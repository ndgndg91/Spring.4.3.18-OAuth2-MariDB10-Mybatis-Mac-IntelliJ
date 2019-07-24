package com.ndgndg91.controller.LoginInterface;

import lombok.Getter;

@Getter
public enum LoginProcess {
    NOT_EXIST_EMAIL("notExistEmail"),
    INCORRECT_PASSWORD("incorrectPassword"),
    LOGIN_SUCCESS("loginSuccess");

    private String message;
    LoginProcess(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
