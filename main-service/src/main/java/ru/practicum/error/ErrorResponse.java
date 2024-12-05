package ru.practicum.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}