package ru.javapractice.dailylunchvoting.model;

public abstract class AbstractNamedBaseEntity extends AbstractBaseEntity {

    String name;

    public AbstractNamedBaseEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
