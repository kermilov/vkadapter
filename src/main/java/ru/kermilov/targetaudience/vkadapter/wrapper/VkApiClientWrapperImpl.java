package ru.kermilov.targetaudience.vkadapter.wrapper;

import java.time.LocalDateTime;

import com.vk.api.sdk.client.VkApiClient;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class VkApiClientWrapperImpl implements VkApiClientWrapper {

    private final VkApiClient vkApiClient;
    private int counter = 0;
    private LocalDateTime lastCall = nowWoNano();
    private LocalDateTime nowWoNano() {
        return LocalDateTime.now().withNano(0);
    }
    @Override
    @SneakyThrows
    public VkApiClient vkApiClient() {
        counter = lastCall.equals(nowWoNano()) ? counter + 1 : 0;
        if (counter > 2) {
            counter = 0;
            Thread.sleep(2000);
        }
        lastCall = nowWoNano();
        return vkApiClient;
    }
}
