package ru.javapractice.dailylunchvoting.model;

import java.util.Date;
import java.util.Set;

public class User extends AbstractNamedBaseEntity {

    private String email;
    private String password;
    private boolean enabled;
    private Date registration;
    private Set<Role> roles;
    private Integer restaurantId;

    public User(Integer id, String name) {
        super(id, name);
    }

}
