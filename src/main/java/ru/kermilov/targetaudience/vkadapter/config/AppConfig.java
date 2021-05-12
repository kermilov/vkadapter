package ru.kermilov.targetaudience.vkadapter.config;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class AppConfig {
    Integer userId;
    String accessToken;
    @Bean
    VkApiClient vkApiClient() {
        return new VkApiClient(HttpTransportClient.getInstance());
    }
    @Bean
    UserActor userActor() {
        return new UserActor(userId, accessToken);
    }
}
