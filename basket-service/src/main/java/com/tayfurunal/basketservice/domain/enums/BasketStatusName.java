package com.tayfurunal.basketservice.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BasketStatusName {

    ACTIVE(1),
    CANCELLED(2),
    COMPLETED(3);

    private final Integer id;
}
