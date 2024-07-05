package ru.practicum.explorewithme.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessage {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}
