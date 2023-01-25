package com.messenger.exp;

public class NotPermissionException extends RuntimeException{
    public NotPermissionException(String message) {
        super(message);
    }
}
