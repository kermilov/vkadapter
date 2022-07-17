package ru.kermilov.targetaudience.vkadapter.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    ARTIST("Артист"),
    RELEASE("Релиз");
    private final String locName;
}
