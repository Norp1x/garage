package com.example.qr.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Created by Norpix on 02.05.2025.
 * Description: General Error Response class
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private int code;
    private String error;
    private String message;
    private String requestUri;
    private LocalDateTime timestamp;
}
