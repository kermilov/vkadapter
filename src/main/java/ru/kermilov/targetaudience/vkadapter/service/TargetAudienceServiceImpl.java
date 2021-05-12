package ru.kermilov.targetaudience.vkadapter.service;

import java.util.List;
import java.util.stream.Collectors;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.kermilov.targetaudience.vkadapter.domain.PostEntity;
import ru.kermilov.targetaudience.vkadapter.event.dto.TargetAudience;
import ru.kermilov.targetaudience.vkadapter.event.transform.TargetAudienceTransform;
import ru.kermilov.targetaudience.vkadapter.wrapper.VkApiClientWrapper;

@Service
@RequiredArgsConstructor
public class TargetAudienceServiceImpl implements TargetAudienceService {

    /**
     * количество друзей, которое нужно вернуть - максимальное значение 1000
     */
    private static final int FRIENDS_MAX_GET_COUNT = 1000;

    @Autowired
    private final VkApiClientWrapper vkApiClientWrapper;
    @Autowired
    private final UserActor actor;

    @Override
    public List<TargetAudience> getByPosts(List<PostEntity> list) {
        return list.stream()
            .map(this::getByPost)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    @SneakyThrows
    private List<TargetAudience> getByPost(PostEntity postEntity) {
        var likesResponse = vkApiClientWrapper.vkApiClient().likes()
            .getList(actor, Type.POST)
            .ownerId(postEntity.getGroupEntity().getExternalId()*-1)
            .itemId(postEntity.getExternalId())
            .execute();
        if (likesResponse.getCount() == 0)
            return List.of();
        var userIds = likesResponse.getItems().stream()
            .map(m -> m.toString())
            .collect(Collectors.joining(","));
        var usersResponse = vkApiClientWrapper.vkApiClient().users()
            .get(actor)
            .userIds(userIds)
            .execute();
        return usersResponse.stream()
            .map(this::userResponseMapper)
            .collect(Collectors.toList());
    }

    private TargetAudience userResponseMapper(GetResponse vkUser) {
        try {
            var friendsResponse = vkApiClientWrapper.vkApiClient().friends()
                .search(actor, vkUser.getId())
                .fields(Fields.CITY)
                .count(FRIENDS_MAX_GET_COUNT)
                .execute();
            var friends = friendsResponse.getItems().stream().map(TargetAudienceTransform::transform)
                .collect(Collectors.toList());
            return TargetAudienceTransform.transform(vkUser, friends);
        } catch (ApiException|ClientException e) {
            return TargetAudienceTransform.transform(vkUser);
        }
    }
}
