package ru.javapractice.dailylunchvoting.model;

import java.util.List;

public class Menu extends AbstractBaseEntity {

    private List<MenuItem> menuItems;

    public Menu(Integer id) {
        super(id);
    }
}
