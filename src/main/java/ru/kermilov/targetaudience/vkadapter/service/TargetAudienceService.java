package ru.kermilov.targetaudience.vkadapter.service;

import java.util.List;

import ru.kermilov.targetaudience.vkadapter.domain.PostEntity;
import ru.kermilov.targetaudience.vkadapter.event.dto.TargetAudience;

public interface TargetAudienceService {
    List<TargetAudience> getByPosts(List<PostEntity> list);

}
