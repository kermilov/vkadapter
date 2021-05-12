package ru.kermilov.targetaudience.vkadapter.service;

import java.util.List;

import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostTemplateEntity;

public interface PostService {
    PostEntity quickInsert(PostTemplateEntity postTemplateEntity, GroupEntity groupEntity);
    PostEntity quickInsert(String url);
    List<PostEntity> refresh(List<PostEntity> list);
}