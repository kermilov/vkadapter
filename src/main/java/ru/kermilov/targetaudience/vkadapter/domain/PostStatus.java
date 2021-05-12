package ru.kermilov.targetaudience.vkadapter.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {
    SUGGEST("Предложено"),
    APPROVE("Одобрено"),
    REJECT("Отклонено");
    private final String locName;
}
