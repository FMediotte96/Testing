package com.junitapp.ejemplos.exception;

public class DineroInsuficienteException extends RuntimeException {

    public DineroInsuficienteException(String message) {
        super(message);
    }
}
