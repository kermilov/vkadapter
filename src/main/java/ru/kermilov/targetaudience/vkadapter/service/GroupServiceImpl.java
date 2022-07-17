package ru.kermilov.targetaudience.vkadapter.service;

import java.util.List;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.Fields;

import com.vk.api.sdk.objects.groups.responses.GetByIdLegacyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;
import ru.kermilov.targetaudience.vkadapter.exception.GroupServiceException;
import ru.kermilov.targetaudience.vkadapter.repository.GroupRepository;
import ru.kermilov.targetaudience.vkadapter.wrapper.VkApiClientWrapper;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private static final String URL_PREFIX = "https://vk.com/public";
    @Autowired
    private final VkApiClientWrapper vkApiClientWrapper;
    @Autowired
    private final UserActor actor;
    @Autowired
    private final GroupRepository repository;

    @Override
    public GroupEntity quickInsert(String url) {
        try {
            var lastIndexOf = url.lastIndexOf("/")+1;
            var id = url.substring(lastIndexOf);
            return getGroupEntity(id);
        } catch (GroupServiceException e) {
            return GroupEntity.builder()
                .url(url)
                .comment(e.getMessage())
                .build();
        }
    }

    @Override
    public GroupEntity quickInsert(Integer externalId) {
        try {
            var id = externalId.toString();
            return getGroupEntity(id);
        } catch (GroupServiceException e) {
            return GroupEntity.builder()
                .externalId(externalId)
                .comment(e.getMessage())
                .build();
        }
    }

    private GroupEntity getGroupEntity(String id) throws GroupServiceException {
        try {
            GetByIdLegacyResponse response = vkApiClientWrapper.vkApiClient().groups()
                    .getByIdLegacy(actor)
                    .groupId(id)
                    .fields(Fields.MEMBERS_COUNT,Fields.CAN_SUGGEST)
                    .execute().get(0);
            if (!response.canSuggest()) {
                throw new GroupServiceException("Can't suggest");
            }
            var entity = new GroupEntity(
                response.getId(),
                response.getMembersCount(),
                response.getName(),
                URL_PREFIX +response.getId());
            repository.findByExternalId(response.getId())
                .map(GroupEntity::getId)
                .ifPresent(entity::setId);
            return repository.save(entity);
        } catch (ApiException|ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<GroupEntity> findAll() {
        return (List<GroupEntity>) repository.findAll();
    }
}
