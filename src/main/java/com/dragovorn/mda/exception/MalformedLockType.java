package com.dragovorn.mda.exception;

/**
 * Just needed some exceptions for lock type errors
 */
public class MalformedLockType extends RuntimeException {

    public MalformedLockType(String type, Throwable error) {
        super(type, error);
    }
}