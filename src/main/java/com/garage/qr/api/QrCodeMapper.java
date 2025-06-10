package com.garage.qr.api;

import com.example.qr.model.QrCodeDto;
import com.garage.qr.domain.QrCode;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING)
public interface QrCodeMapper {

    QrCodeDto toDto(QrCode qrCode);

    QrCode toQrCode(QrCodeDto qrCodeDto);
}
