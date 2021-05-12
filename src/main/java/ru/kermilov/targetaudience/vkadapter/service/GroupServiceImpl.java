package ru.kermilov.targetaudience.vkadapter.service;

import java.util.List;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.Fields;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;
import ru.kermilov.targetaudience.vkadapter.repository.GroupRepository;
import ru.kermilov.targetaudience.vkadapter.wrapper.VkApiClientWrapper;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    @Autowired
    private final VkApiClientWrapper vkApiClientWrapper;
    @Autowired
    private final UserActor actor;
    @Autowired
    private final GroupRepository repository;

    @Override
    public GroupEntity quickInsert(String url) {
        GroupEntity result = null;
        var lastIndexOf = url.lastIndexOf("/")+1;
        var id = url.substring(lastIndexOf);
        try {
            var response = vkApiClientWrapper.vkApiClient().groups().getByIdLegacy(actor)
                .groupId(id)
                .fields(Fields.MEMBERS_COUNT)
                .execute().get(0);
            var entity = new GroupEntity(
                response.getId(),
                response.getMembersCount(),
                response.getName(),
                url.substring(0, lastIndexOf) +"public"+response.getId());
            result = repository.save(entity);
        } catch (ApiException|ClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public GroupEntity quickInsert(Integer externalId) {
        GroupEntity result = null;
        try {
            var response = vkApiClientWrapper.vkApiClient().groups()
                .getByIdLegacy(actor)
                .groupId(externalId.toString())
                .fields(Fields.MEMBERS_COUNT)
                .execute().get(0);
            var entity = new GroupEntity(
                response.getId(),
                response.getMembersCount(),
                response.getName(),
                "https://vk.com/public"+response.getId());
            result = repository.save(entity);
        } catch (ApiException|ClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
