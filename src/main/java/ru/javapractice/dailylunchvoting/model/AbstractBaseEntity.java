package ru.javapractice.dailylunchvoting.model;

public abstract class AbstractBaseEntity {

    Integer id;

    public AbstractBaseEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
