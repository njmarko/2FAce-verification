package com.xor.FAce.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoAuthorityException extends RuntimeException {
    public NoAuthorityException() {
        super("Provided user has no authority set.");
    }
}
