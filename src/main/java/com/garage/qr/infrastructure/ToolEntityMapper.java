package com.garage.qr.infrastructure;

import com.garage.qr.domain.Tool;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING)
public interface ToolEntityMapper {

    ToolEntity mapToEntity(Tool tool);

    Tool mapToTool(ToolEntity toolEntity);
}
