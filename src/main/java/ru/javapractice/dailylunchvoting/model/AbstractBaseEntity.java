package ru.javapractice.dailylunchvoting.model;

import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;

import javax.persistence.*;

import static org.hibernate.proxy.HibernateProxyHelper.getClassWithoutInitializingProxy;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractBaseEntity implements Persistable<Integer> {
    public static final int START_SEQ = 100000;

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    protected Integer id;

    protected AbstractBaseEntity() {
    }

    public AbstractBaseEntity(Integer id) {
        this.id = id;
    }

    public int id() {
        Assert.notNull(id, "Entity must have id");
        return id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }

    @SuppressWarnings("EqualsDoesntCheckParameterClass -https://stackoverflow.com/a/78077907/548473")
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClassWithoutInitializingProxy(this) != getClassWithoutInitializingProxy(o)) return false;
        return getId() != null && getId().equals(((AbstractBaseEntity) o).getId());
    }

    @Override
    public final int hashCode() {
        return getClassWithoutInitializingProxy(this).hashCode();
    }
}
