package ru.practicum.exception;

import lombok.Getter;

@Getter
public class ViolationRestrictException extends RuntimeException {
    private String reason;
    private String status;

    public ViolationRestrictException(String status, String reason, String message) {
        super(message);
        this.reason = reason;
        this.status = status;
    }
}
