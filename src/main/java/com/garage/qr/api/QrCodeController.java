package com.garage.qr.api;

import com.garage.qr.domain.GenerateQrImageService;
import com.garage.qr.model.ToolDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Norpix on 02.05.2025.
 * Description: Rest Controller Class for application
 */
@RestController
@RequestMapping("/garage")
public class QrCodeController {

    private final GenerateQrImageService generateQrImageService;
    private final ToolMapper toolMapper;
    private final QrCodeMapper qrCodeMapper;

    public QrCodeController(GenerateQrImageService generateQrImageService, ToolMapper toolMapper, QrCodeMapper qrCodeMapper) {
        this.generateQrImageService = generateQrImageService;
        this.toolMapper = toolMapper;
        this.qrCodeMapper = qrCodeMapper;
    }

    @GetMapping("/")
    public String getHomepage(Model model) {
        model.addAttribute("qrCode", "Home Page");
        return "index";
    }

    @GetMapping("/read-qr-code")
    public String getValueFromQrCode(String qrCode) {
        if (StringUtils.isBlank(qrCode)) {
            throw new IllegalArgumentException("QR code is empty");
        }
        return "QrCode: " + qrCode;
    }

    @PostMapping("/generate-qr-code")
    public String generateQrCode(@RequestBody ToolDto toolDto, Model model) {
        if (toolDto == null) {
            throw new IllegalArgumentException("Tool request is empty");
        }
        final var qrCode = generateQrImageService.generateQRCode(toolMapper.toTool(toolDto));
        qrCodeMapper.toDto(qrCode);
        model.addAttribute("qrCode", qrCode);
        return "index";
    }
}
