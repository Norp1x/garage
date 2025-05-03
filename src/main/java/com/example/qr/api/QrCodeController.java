package com.example.qr.api;

import com.example.qr.domain.GenerateQrImageService;
import com.example.qr.model.QrCodeDto;
import com.example.qr.model.ToolDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Norpix on 02.05.2025.
 * Description: Rest Controller Class for application
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/garage")
public class QrCodeController {

    private final GenerateQrImageService generateQrImageService;
    private final ToolMapper toolMapper;
    private final QrCodeMapper qrCodeMapper;

    @GetMapping("/read-qr-code")
    public String getValueFromQrCode(String qrCode) {
        if (StringUtils.isBlank(qrCode)) {
            throw new IllegalArgumentException("QR code is empty");
        }
        return "QrCode: " + qrCode;
    }

    @PostMapping("/generate-qr-code")
    public QrCodeDto generateQrCode(@RequestBody ToolDto toolDto) {
        if (toolDto == null) {
            throw new IllegalArgumentException("Tool request is empty");
        }
        final var qrCode = generateQrImageService.generateQRCode(toolMapper.toTool(toolDto));
        return qrCodeMapper.toDto(qrCode);
    }
}
