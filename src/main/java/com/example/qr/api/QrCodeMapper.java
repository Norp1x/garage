package com.example.qr.api;

import com.example.qr.domain.QrCode;
import com.example.qr.model.QrCodeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Created by Norpix on 03.05.2025.
 * Description: Mapstruct tools mapping class
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING)
public interface QrCodeMapper {

    QrCodeDto toDto(QrCode qrCode);

    QrCode toQrCode(QrCodeDto qrCodeDto);
}
