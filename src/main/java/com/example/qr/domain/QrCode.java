package com.example.qr.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Norpix on 03.05.2025.
 * Description: QR Code class with data
 */
@Getter
@Setter
@Builder
public class QrCode {

    String name;
    String image;
}
