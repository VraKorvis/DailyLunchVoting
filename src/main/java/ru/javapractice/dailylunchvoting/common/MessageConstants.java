package ru.javapractice.dailylunchvoting.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageConstants {
    public static final String MENU_ALREADY_EXISTS = "Menu for restaurant with id=%d for today already exists";
    public static final String MENU_NOT_ASSIGNED = "Menu for restaurant with id=%d for date %s has not been assigned yet";

    public static final String RESTAURANT_NOT_FOUND = "Restaurant with id=%d not found";

    public static final String MENU_ITEMS_NOT_FOUND = "MenuItems not found for IDs: %s";
    public static final String MENU_ITEM_NOT_FOUND = "MenuItem with id=%d not found";
    public static final String MENU_ITEM_PRICE_NULL = "Price must not be null (menuItem id=%s)";
    public static final String CANNOT_MODIFY_PAST_DATE = "Cannot create/edit menu for past date %s";
    public static final String CANNOT_MODIFY_AFTER_VOTING = "Menu for today cannot be modified or added after %s";

    public static final String VOTE_UPDATED = "Your vote has been successfully updated";
    public static final String VOTE_ACCEPTED = "Your vote has been successfully accepted";
    public static final String VOTING_ENDED_CANNOT_CHANGE = "Voting period has ended, you can no longer change your vote";
    public static final String VOTING_ENDED_CANNOT_VOTE = "Voting period has ended, you can no longer vote";

}


