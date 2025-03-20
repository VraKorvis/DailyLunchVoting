package ru.javapractice.dailylunchvoting.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@MappedSuperclass
public abstract class AbstractNamedBaseEntity extends AbstractBaseEntity {

    @NotBlank
    @Size(min = 2, max = 128)
    @Column(name = "name", nullable = false)
    String name;

    protected AbstractNamedBaseEntity() {
    }

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
