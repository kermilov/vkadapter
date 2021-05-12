package ru.kermilov.targetaudience.vkadapter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.kermilov.targetaudience.vkadapter.domain.PostTemplateEntity;
import ru.kermilov.targetaudience.vkadapter.repository.PostTemplateRepository;

@SpringBootTest
class PostTemplateServiceTest {

    @Autowired
    PostTemplateService service;
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    VkApiClient vkApiClient;
    @MockBean
    PostTemplateRepository repository;
    @Captor
    ArgumentCaptor<PostTemplateEntity> captor;

    @Test
    void quickInsertPlaylistUrlTest() throws ApiException, ClientException, IOException {
        // мокируем клиента ВК
        when(vkApiClient.wall()
            .getByIdLegacy(any(UserActor.class), eq(TestConstants.wallGetByIdLegacyInVkMusicians()))
            .execute()).thenReturn(List.of(TestConstants.wallGetByIdLegacyOutVkMusicians()));
        when(vkApiClient
            .execute()
            .code(any(UserActor.class), eq(TestConstants.executeWallInVkMusicians()))
            .execute()).thenReturn(TestConstants.executeWallOutVkMusicians());
        // тестируемый метод
        service.quickInsert(TestConstants.VKMUSICIANS_POST_URL);
        // что ожидаем
        var expected = TestConstants.getPostTemplateEntityVkMusicians();
        // что получили
        verify(repository).save(captor.capture());
        var actual = captor.getValue();
        // сравниваем
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void quickInsertPhotoAudioUrlTest() throws ApiException, ClientException, IOException {
        // мокируем клиента ВК
        when(vkApiClient.wall()
            .getByIdLegacy(any(UserActor.class), eq(TestConstants.wallGetByIdLegacyInEMusicYar()))
            .execute()).thenReturn(List.of(TestConstants.wallGetByIdLegacyOutEMusicYar()));
        when(vkApiClient
            .execute()
            .code(any(UserActor.class), eq(TestConstants.executeWallInEMusicYar()))
            .execute()).thenReturn(TestConstants.executeWallOutEMusicYar());
        // тестируемый метод
        service.quickInsert(TestConstants.E_MUSIC_YAR_POST_URL);
        // что ожидаем
        var expected = TestConstants.getPostTemplateEntityEMusicYar();
        // что получили
        verify(repository).save(captor.capture());
        var actual = captor.getValue();
        // сравниваем
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
