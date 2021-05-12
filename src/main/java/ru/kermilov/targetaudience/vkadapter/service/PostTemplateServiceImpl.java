package ru.kermilov.targetaudience.vkadapter.service;

import java.util.List;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.kermilov.targetaudience.vkadapter.domain.PostTemplateEntity;
import ru.kermilov.targetaudience.vkadapter.repository.PostTemplateRepository;
import ru.kermilov.targetaudience.vkadapter.wrapper.VkApiClientWrapper;

@Service
@RequiredArgsConstructor
public class PostTemplateServiceImpl implements PostTemplateService {

    @Autowired
    private final VkApiClientWrapper vkApiClientWrapper;
    @Autowired
    private final UserActor actor;
    @Autowired
    private final PostTemplateRepository repository;
    @Autowired
    private final AttachService attachService;
    @Override
    public PostTemplateEntity quickInsert(String url) {
        PostTemplateEntity result = null;
        var lastIndexOf = url.lastIndexOf("wall")+4;
        var id = url.substring(lastIndexOf);
        try {
            var response = vkApiClientWrapper.vkApiClient().wall()
                .getByIdLegacy(actor, id)
                .execute().get(0);
            var entity = new PostTemplateEntity(
                response.getText(),
                attachService.parse(id));
            result = repository.save(entity);
        } catch (ApiException|ClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
