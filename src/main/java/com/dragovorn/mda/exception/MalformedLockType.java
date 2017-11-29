package com.dragovorn.mda.exception;

public class MalformedLockType extends RuntimeException {

    public MalformedLockType(String type, Throwable error) {
        super(type, error);
    }
}