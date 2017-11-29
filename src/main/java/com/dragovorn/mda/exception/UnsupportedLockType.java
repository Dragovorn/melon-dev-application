package com.dragovorn.mda.exception;

public class UnsupportedLockType extends Exception {

    public UnsupportedLockType(String type) {
        super(type);
    }
}