package ru.kermilov.targetaudience.vkadapter.event.publisher;

import java.util.Collection;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.kermilov.targetaudience.vkadapter.event.dto.TargetAudience;

@Component
@RequiredArgsConstructor
public class TargetAudiencePublisher {
    private final TargetAudienceGateway gateway;

    public void targetAudiencePublish(Collection<TargetAudience> targetAudience) {
        targetAudience.forEach(gateway::targetAudienceTopicPublish);
    }
}
