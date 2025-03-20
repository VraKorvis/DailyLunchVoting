package ru.javapractice.dailylunchvoting.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@MappedSuperclass
// http://stackoverflow.com/questions/594597/hibernate-annotations-which-is-better-field-or-property-access
@Access(AccessType.FIELD)
public abstract class AbstractBaseEntity implements Persistable<Integer> {
    public static final int START_SEQ = 100000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
    //  See https://hibernate.atlassian.net/browse/HHH-3718 and https://hibernate.atlassian.net/browse/HHH-12034
//  Proxy initialization when accessing its identifier managed now by JPA_PROXY_COMPLIANCE setting
    protected Integer id;

    protected AbstractBaseEntity() {
    }

    public AbstractBaseEntity(Integer id) {
        this.id = id;
    }

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
}
