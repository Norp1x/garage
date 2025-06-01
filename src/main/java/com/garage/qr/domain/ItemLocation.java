package com.garage.qr.domain;

/**
 * Created by Norpix on 01.06.2025.
 * Description: Object class for location of items
 */
public record ItemLocation(String name,
                           String color,
                           QrCode qrCode) {
}
