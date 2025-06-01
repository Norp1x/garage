package com.garage.qr.api;

import com.garage.qr.domain.Tool;
import com.garage.qr.model.ToolDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Created by Norpix on 03.05.2025.
 * Description: Mapstruct tools mapping class
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING)
public interface ToolMapper {

    ToolDto toDto(Tool tool);

    Tool toTool(ToolDto toolDto);
}
