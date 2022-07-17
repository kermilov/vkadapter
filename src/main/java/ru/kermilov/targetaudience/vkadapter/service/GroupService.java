package ru.kermilov.targetaudience.vkadapter.service;

import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;

import java.util.List;

public interface GroupService {
    GroupEntity quickInsert(String url);
    GroupEntity quickInsert(Integer externalId);
    List<GroupEntity> findAll();
}