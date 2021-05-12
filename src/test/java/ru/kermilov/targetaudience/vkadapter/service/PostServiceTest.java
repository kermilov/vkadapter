package ru.kermilov.targetaudience.vkadapter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.Fields;
import com.vk.api.sdk.objects.groups.responses.GetByIdLegacyResponse;
import com.vk.api.sdk.objects.wall.PostType;
import com.vk.api.sdk.objects.wall.responses.PostResponse;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostStatus;
import ru.kermilov.targetaudience.vkadapter.domain.PostTemplateEntity;
import ru.kermilov.targetaudience.vkadapter.event.publisher.TargetAudiencePublisher;
import ru.kermilov.targetaudience.vkadapter.repository.PostRepository;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService service;
    @MockBean
    PostRepository repository;
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    VkApiClient vkApiClient;
    @Captor
    ArgumentCaptor<PostEntity> captor;
    @MockBean
    TargetAudienceService targetAudienceService;
    @MockBean
    TargetAudiencePublisher targetAudiencePublisher;

    @Test
    void quickInsertByTemplateTest() throws ApiException, ClientException {
        // мокируем клиента ВК
        var postResponse = new PostResponse();
        postResponse.setPostId(TestConstants.VKMUSICIANS_POST_ID);
        when(vkApiClient.wall()
            .post(any())
            .ownerId(any())
            .message(any())
            .attachments(anyString())
            .execute()).thenReturn(postResponse);
        // входные данные
        var postTemplateEntity = TestConstants.getPostTemplateEntityVkMusicians();
        var groupEntity = TestConstants.getGroupEntityVkMusicians();
        // тестируемый метод
        service.quickInsert(postTemplateEntity, groupEntity);
        // что ожидаем
        var expected = TestConstants.getPostEntityVkMusicians();
        // что получили
        verify(repository).save(captor.capture());
        var actual = captor.getValue();
        // сравниваем
        assertThat(actual).usingRecursiveComparison()
            .ignoringFields("externalId","url")
            .isEqualTo(expected);
        assertThat(actual.getExternalId()).isEqualTo(TestConstants.VKMUSICIANS_POST_ID);
        assertThat(actual.getUrl()).isEqualTo("https://vk.com/wall"+TestConstants.VKMUSICIANS_ID*-1+"_"+TestConstants.VKMUSICIANS_POST_ID);
    }

    @Test
    void quickInsertByUrlTest() throws ApiException, ClientException, IOException {
        // мокируем клиента ВК
        when(vkApiClient.groups()
            .getByIdLegacy(any(UserActor.class))
            .groupId(eq(TestConstants.groupGetByIdLegacyInVkMusiciansId()))
            .fields(any(Fields.class))
            .execute()).thenReturn(List.of(TestConstants.groupGetByIdLegacyOutVkMusicians()));
        when(vkApiClient.wall()
            .getByIdLegacy(any(UserActor.class), eq(TestConstants.wallGetByIdLegacyInVkMusicians()))
            .execute()).thenReturn(List.of(TestConstants.wallGetByIdLegacyOutVkMusicians()));
        when(vkApiClient
            .execute()
            .code(any(UserActor.class), eq(TestConstants.executeWallInVkMusicians()))
            .execute()).thenReturn(TestConstants.executeWallOutVkMusicians());
        // что ожидаем
        var expected = TestConstants.getPostEntityVkMusicians();
        expected.setStatus(PostStatus.APPROVE);
        // что получили
        service.quickInsert(TestConstants.VKMUSICIANS_POST_URL);
        verify(repository).save(captor.capture());
        var actual = captor.getValue();
        // сравниваем
        assertThat(actual).usingRecursiveComparison()
            .ignoringFields("id","groupEntity.id","groupEntity.membersCount")
            .isEqualTo(expected);
    }

    @Test
    void refreshTest() throws ApiException, ClientException {
        // мокируем клиента ВК
        when(vkApiClient.wall()
            .getByIdLegacy(any(UserActor.class), eq(TestConstants.wallGetByIdLegacyInVkMusicians()+","+TestConstants.wallGetByIdLegacyInEMusicYar()))
            .execute()).thenReturn(List.of(TestConstants.wallGetByIdLegacyOutVkMusicians()));
        // входные данные
        var postEntity1 = TestConstants.getPostEntityVkMusicians();
        var postEntity2 = TestConstants.getPostEntityEMusicYar();
        // что получили
        service.refresh(List.of(postEntity1,postEntity2));
        // сравниваем
        assertThat(postEntity1.getStatus())
            .isEqualTo(PostStatus.APPROVE);
        assertThat(postEntity2.getStatus())
            .isEqualTo(PostStatus.REJECT);
    }

}
