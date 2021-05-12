package ru.kermilov.targetaudience.vkadapter.event.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import ru.kermilov.targetaudience.vkadapter.event.Constants;

public interface ProducerChannels {

    @Output(Constants.TARGET_AUDIENCE_TOPIC_PUBLISH)
    MessageChannel targetAudienceTopicPublish();
}