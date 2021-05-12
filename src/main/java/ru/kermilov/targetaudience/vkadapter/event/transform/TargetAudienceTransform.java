package ru.kermilov.targetaudience.vkadapter.event.transform;

import java.util.List;
import java.util.Optional;

import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.kermilov.targetaudience.vkadapter.event.dto.TargetAudience;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TargetAudienceTransform {

    public static TargetAudience transform(UserFull vkUser) {
        var city = vkUser.getCity() != null ? vkUser.getCity().getTitle() : null;
        return new TargetAudience(vkUser.getId(), vkUser.getFirstName(), vkUser.getLastName(), city);
    }

    public static TargetAudience transform(GetResponse vkUser) {
        var city = vkUser.getCity() != null ? vkUser.getCity().getTitle() : null;
        return new TargetAudience(vkUser.getId(), vkUser.getFirstName(), vkUser.getLastName(), city);
    }

    public static TargetAudience transform(GetResponse vkUser, List<TargetAudience> friends) {
        var targetAudience = transform(vkUser);
        targetAudience.setFriends(friends);
        return targetAudience;
    }

}
