package ru.kermilov.targetaudience.vkadapter.event.publisher;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import ru.kermilov.targetaudience.vkadapter.event.Constants;
import ru.kermilov.targetaudience.vkadapter.event.dto.TargetAudience;

@MessagingGateway
public interface TargetAudienceGateway {

    @Gateway(requestChannel = Constants.TARGET_AUDIENCE_TOPIC_PUBLISH)
    void targetAudienceTopicPublish(TargetAudience targetAudience);

}