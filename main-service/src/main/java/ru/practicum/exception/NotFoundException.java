package ru.practicum.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private String reason;
    private String status;

    public NotFoundException(String status, String reason, String message) {
        super(message);
        this.reason = reason;
        this.status = status;
    }
}