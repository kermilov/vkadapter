package ru.kermilov.targetaudience.vkadapter.service;

import ru.kermilov.targetaudience.vkadapter.domain.GroupTemplateEntity;

import java.util.List;

public interface GroupTemplateService {
    List<GroupTemplateEntity> findAll();
}