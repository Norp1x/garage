package com.garage.qr.domain;

import lombok.Builder;

/**
 * Created by Norpix on 03.05.2025.
 * Description: QR Code class with data
 */
@Builder
public record QrCode(String name,
                     String image) {
}
