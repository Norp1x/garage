package com.garage.qr.common;

import java.time.LocalDateTime;

/**
 * Created by Norpix on 02.05.2025.
 * Description: General Error Response class
 */
public record ErrorResponse(int code,
                            String error,
                            String message,
                            String requestUri,
                            LocalDateTime timestamp) {
}
