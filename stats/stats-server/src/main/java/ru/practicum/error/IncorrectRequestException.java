package ru.practicum.error;

import lombok.Getter;

@Getter
public class IncorrectRequestException extends RuntimeException {
    private String reason;
    private String status;

    public IncorrectRequestException(String status, String reason, String message) {
        super(message);
        this.reason = reason;
        this.status = status;;
    }
}
