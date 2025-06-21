package com.yazilim.afet.exception;

import java.util.List;

public class ConflictException extends RuntimeException {

    private final List<String> errors;

    public ConflictException(List<String> errors) {
        super("Validation conflict occurred");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
