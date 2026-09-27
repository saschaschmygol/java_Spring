package ru.shmygol.MySeconddTestAppSpringBoot.exception;

public class ValidationFailedException extends Exception {
    public ValidationFailedException(String message){
        super(message);
    }
}
