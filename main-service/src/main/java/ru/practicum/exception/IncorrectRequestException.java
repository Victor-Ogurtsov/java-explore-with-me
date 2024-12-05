package ru.practicum.exception;

import lombok.Getter;

@Getter
public class IncorrectRequestException extends RuntimeException {
    String reason;
    String status;

    public IncorrectRequestException(String status, String reason, String message) {
        super(message);
        this.reason = reason;
        this.status = status;;
    }
}
