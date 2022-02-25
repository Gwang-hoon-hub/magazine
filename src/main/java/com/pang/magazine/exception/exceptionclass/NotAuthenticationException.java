package com.pang.magazine.exception.exceptionclass;

import org.springframework.security.core.AuthenticationException;

public class NotAuthenticationException extends IllegalArgumentException {
    public NotAuthenticationException(String s) {
        super(s);
    }
}
