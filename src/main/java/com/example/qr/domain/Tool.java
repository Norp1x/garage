package com.example.qr.domain;

import lombok.*;

/**
 * Created by Norpix on 02.05.2025.
 * Description: Tool object class with data
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tool {

    String name;
    String description;
    String size;
    String color;
}
