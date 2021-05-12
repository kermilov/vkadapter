package ru.kermilov.targetaudience.vkadapter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.objects.users.Fields;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.kermilov.targetaudience.vkadapter.event.dto.TargetAudience;
import ru.kermilov.targetaudience.vkadapter.event.publisher.TargetAudiencePublisher;

@SpringBootTest
class TargetAudienceServiceTest {

    @Autowired
    TargetAudienceService targetAudienceService;
    @MockBean
    TargetAudiencePublisher targetAudiencePublisher;
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    VkApiClient vkApiClient;

    @Test
    void getByPostsTest() throws ApiException, ClientException {
        // мокируем клиента ВК
        var postEntity = TestConstants.getPostEntityVkMusicians();
        var likesResponse = TestConstants.getLikesResponseAuthor();
        when(vkApiClient.likes()
            .getList(any(UserActor.class), eq(Type.POST))
            .ownerId(eq(postEntity.getGroupEntity().getExternalId()*-1))
            .itemId(eq(postEntity.getExternalId()))
            .execute()).thenReturn(likesResponse);
        var vkUser = TestConstants.getUsersResponseAuthor();
        when(vkApiClient.users()
            .get(any(UserActor.class))
            .userIds(eq(String.valueOf(TestConstants.AUTHOR_ID)))
            .execute()).thenReturn(List.of(vkUser));
        var friendsResponse = TestConstants.getFriendsSearchResponseEmpty();
        when(vkApiClient.friends()
            .search(any(UserActor.class), eq(vkUser.getId()))
            .fields(eq(Fields.CITY))
            .count(any())
            .execute()).thenReturn(friendsResponse);
        // входные данные
        var list = List.of(postEntity);
        // что ожидаем
        var expected = List.of(
            new TargetAudience(TestConstants.AUTHOR_ID, TestConstants.FIRST_NAME, TestConstants.LAST_NAME, TestConstants.CITY, List.of())
        );
        // что получили
        var actual = targetAudienceService.getByPosts(list);
        // сравниваем
        assertThat(actual)
            .usingElementComparatorIgnoringFields("friends")
            .isEqualTo(expected);
    }
}
