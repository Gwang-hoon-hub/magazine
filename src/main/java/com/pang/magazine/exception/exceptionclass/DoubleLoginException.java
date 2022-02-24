package com.pang.magazine.exception.exceptionclass;

public class DoubleLoginException extends IllegalArgumentException{
    public DoubleLoginException(String s) {
        super(s);
    }
}
