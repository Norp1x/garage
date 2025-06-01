package com.garage.qr.domain;

import lombok.Builder;

/**
 * Created by Norpix on 02.05.2025.
 * Description: Tool object class with data
 */
@Builder
public record Tool(String name,
                   String description,
                   String size,
                   String color,
                   String toolPlacing) {
}
