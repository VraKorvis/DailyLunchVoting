package ru.javapractice.dailylunchvoting.model;

public class MenuItem extends AbstractNamedBaseEntity {

    private Float price;

    public MenuItem(Integer id, String name) {
        super(id, name);
    }
}
