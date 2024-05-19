package com.github.beeflang.dairy;

public class ArgumentNotPresentException extends IllegalStateException {
    public ArgumentNotPresentException(String message) {
        super(message);
    }
}