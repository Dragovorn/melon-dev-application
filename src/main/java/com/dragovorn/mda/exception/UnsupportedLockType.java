package com.dragovorn.mda.exception;

/**
 * Just needed some exceptions for lock type errors
 */
public class UnsupportedLockType extends Exception {

    public UnsupportedLockType(String type) {
        super(type);
    }
}