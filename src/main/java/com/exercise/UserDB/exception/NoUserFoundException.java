package com.exercise.UserDB.exception;

public class NoUserFoundException extends RuntimeException {

    public NoUserFoundException(String message) {
        super(message);
    }
}
