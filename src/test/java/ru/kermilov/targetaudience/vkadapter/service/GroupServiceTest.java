package ru.kermilov.targetaudience.vkadapter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.Fields;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;
import ru.kermilov.targetaudience.vkadapter.repository.GroupRepository;

@SpringBootTest
class GroupServiceTest {

    @Autowired
    GroupService service;
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    VkApiClient vkApiClient;
    @MockBean
    GroupRepository repository;
    @Captor
    ArgumentCaptor<GroupEntity> captor;

    @Test
    void quickInsertTest() throws ApiException, ClientException {
        // мокируем клиента ВК
        when(vkApiClient.groups()
            .getByIdLegacy(any(UserActor.class))
            .groupId(eq(TestConstants.groupGetByIdLegacyInVkMusiciansScreenName()))
            .fields(any(Fields.class))
            .execute()).thenReturn(List.of(TestConstants.groupGetByIdLegacyOutVkMusicians()));
        // тестируемый метод
        service.quickInsert(TestConstants.VKMUSICIANS_URL_SCREEN_NAME);
        // что ожидаем
        var expected = TestConstants.getGroupEntityVkMusicians();
        // что получили
        verify(repository).save(captor.capture());
        var actual = captor.getValue();
        // сравниваем
        assertThat(actual).usingRecursiveComparison()
            .ignoringFields("membersCount")
            .isEqualTo(expected);
    }

}
