package com.stepthrone.exception.customexception;

public class StepDataNotFoundException extends RuntimeException {
    public StepDataNotFoundException(String message) {
        super(message);
    }
}