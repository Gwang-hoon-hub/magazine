package com.pang.magazine.exception.exceptionclass;

public class NotFoundException extends IllegalArgumentException{
    public NotFoundException(String msg) {
        super(msg);
    }
}
