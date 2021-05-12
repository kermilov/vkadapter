package ru.kermilov.targetaudience.vkadapter.service;

import java.util.List;
import java.util.stream.Collectors;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.PostType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostStatus;
import ru.kermilov.targetaudience.vkadapter.domain.PostTemplateEntity;
import ru.kermilov.targetaudience.vkadapter.event.publisher.TargetAudiencePublisher;
import ru.kermilov.targetaudience.vkadapter.repository.PostRepository;
import ru.kermilov.targetaudience.vkadapter.wrapper.VkApiClientWrapper;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    @Autowired
    private final VkApiClientWrapper vkApiClientWrapper;
    @Autowired
    private final UserActor actor;
    @Autowired
    private final PostRepository repository;
    @Autowired
    private final AttachService attachService;
    @Autowired
    private final GroupService groupService;
    @Autowired
    private final TargetAudienceService targetAudienceService;
    @Autowired
    private final TargetAudiencePublisher targetAudiencePublisher;

    @Override
    public PostEntity quickInsert(PostTemplateEntity postTemplateEntity, GroupEntity groupEntity) {
        var entity = new PostEntity(groupEntity, postTemplateEntity.getMessage(), postTemplateEntity.getAttachment());
        try {
            var ownerId = entity.getGroupEntity().getExternalId()*-1;
            var response = vkApiClientWrapper.vkApiClient().wall()
                .post(actor)
                .ownerId(ownerId)
                .message(entity.getMessage())
                .attachments(entity.getAttachment())
                .execute();
            entity.setExternalIdAndUrl(response.getPostId());
        } catch (ApiException|ClientException e) {
            entity.setComment("quickInsert error: " + e.getMessage());
        }
        return repository.save(entity);
    }

    @Override
    public PostEntity quickInsert(String url) {
        PostEntity result = null;
        var lastIndexOf = url.lastIndexOf("wall")+4;
        var id = url.substring(lastIndexOf);
        try {
            var response = vkApiClientWrapper.vkApiClient().wall()
                .getByIdLegacy(actor, id)
                .execute().get(0);
            var groupEntity = groupService.quickInsert(response.getOwnerId()*-1);
            var entity = new PostEntity(groupEntity,
                response.getText(),
                attachService.parse(id));
            entity.setExternalIdAndUrl(response.getId());
            entity.setStatus(PostStatus.APPROVE);
            result = repository.save(entity);
        } catch (ApiException|ClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<PostEntity> refresh(List<PostEntity> list) {
        var result = refreshStatus(list);
        targetAudiencePublish(result);
        return result;
    }

    private List<PostEntity> refreshStatus(List<PostEntity> list) {
        try {
            var posts = list.stream()
                .map(PostEntity::getVKApiClientId)
                .collect(Collectors.joining(","));
            var wallResponse = vkApiClientWrapper.vkApiClient().wall()
                .getByIdLegacy(actor, posts)
                .execute();
            // предложенные посты
            var suggestList = list.stream()
                .filter(f -> f.getStatus() == PostStatus.SUGGEST)
                .collect(Collectors.toList());
            // одобрили
            wallResponse.stream()
                // если тип в ВК поменялся с предложки
                .filter(f -> f.getPostType() != PostType.SUGGEST)
                .forEach(r ->
                    suggestList.stream()
                        .filter(f -> f.getVKApiClientId().equalsIgnoreCase(r.getOwnerId()+"_"+r.getId()))
                        .findFirst()
                        // синхроним это с нашей базой
                        .ifPresent(p -> p.setStatus(PostStatus.APPROVE))
                );
            // или отклонили
            suggestList.stream()
                .filter(f ->
                    // если в ВК поста нет
                    wallResponse.stream()
                        .filter(r -> f.getVKApiClientId().equalsIgnoreCase(r.getOwnerId()+"_"+r.getId()))
                        .count() == 0)
                .collect(Collectors.toList())
                // синхроним это с нашей базой
                .forEach(p -> p.setStatus(PostStatus.REJECT));
        } catch (ApiException|ClientException e) {
            e.printStackTrace();
        }
        return (List<PostEntity>) repository.saveAll(list);
    }

    private void targetAudiencePublish(List<PostEntity> list) {
        // c одобренных постов
        var approveList = list.stream()
            .filter(f -> f.getStatus() == PostStatus.APPROVE)
            .collect(Collectors.toList());
        // собираем целевую аудиторию
        var targetAudience = targetAudienceService.getByPosts(approveList);
        // и публикуем в брокер сообщений
        targetAudiencePublisher.targetAudiencePublish(targetAudience);
    }
}
