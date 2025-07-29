package com.garage.qr.infrastructure;

import com.garage.qr.domain.Tool;

import java.util.Optional;

/**
 * Created by  on 29.07.2025.
 * Description:
 */
public interface ToolRepository {

    Optional<Tool> getToolByName(String name);
}
