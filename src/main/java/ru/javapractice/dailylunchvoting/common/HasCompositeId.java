package ru.javapractice.dailylunchvoting.common;

import org.springframework.util.Assert;

public interface HasCompositeId<T> {
    T getId();

    default T id() {
        Assert.notNull(getId(), "Entity must have composite id");
        return getId();
    }
}
