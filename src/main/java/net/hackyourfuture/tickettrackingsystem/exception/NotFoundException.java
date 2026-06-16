package net.hackyourfuture.tickettrackingsystem.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String field;

    public NotFoundException(String message) {
        super(message);
        this.field = null;
    }

    public NotFoundException(String message, String field) {
        super(message);
        this.field = field;
    }
}
