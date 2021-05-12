package ru.kermilov.targetaudience.vkadapter.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    /**
     * Координаты топика, откуда забирает данные центральный сервис
     */
    public static final String TARGET_AUDIENCE_TOPIC_PUBLISH = "targetAudienceTopicPublish";

}
