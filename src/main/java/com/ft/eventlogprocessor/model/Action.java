package com.ft.eventlogprocessor.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Action {
    LOGIN,
    LOGOUT,
    VIEW,
    CLICK,
    PURCHASE;

    @JsonCreator
    public static Action fromString(String value) {

        if (value == null) {
            return null;
        }

        return Action.valueOf(value.trim().toUpperCase());

    }
}
