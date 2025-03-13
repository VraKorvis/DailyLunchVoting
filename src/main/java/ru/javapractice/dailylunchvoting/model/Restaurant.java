package ru.javapractice.dailylunchvoting.model;

import java.util.Arrays;

public class Restaurant extends AbstractNamedBaseEntity {

    private Menu menu;

    public Restaurant(Integer id, String name, Menu menu) {
        super(id, name);
        this.menu = menu;
    }
}
