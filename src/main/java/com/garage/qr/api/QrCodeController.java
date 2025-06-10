package com.garage.qr.api;

import com.example.qr.model.QrCodeDto;
import com.example.qr.model.ToolDto;
import com.garage.qr.domain.GenerateQrImageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/garage")
public class QrCodeController {

    private final GenerateQrImageService generateQrImageService;
    private final ToolMapper toolMapper;
    private final QrCodeMapper qrCodeMapper;

    public QrCodeController(GenerateQrImageService generateQrImageService,
                            ToolMapper toolMapper,
                            QrCodeMapper qrCodeMapper) {
        this.generateQrImageService = generateQrImageService;
        this.toolMapper = toolMapper;
        this.qrCodeMapper = qrCodeMapper;
    }

    @PostMapping("/generate-qr-code")
    public ResponseEntity<QrCodeDto> generateQrCode(@Valid @RequestBody ToolDto toolDto) {
        log.info("Generating QR code for tool: {}", toolDto.getName());

        final var qrCode = generateQrImageService.generateQRCode(toolMapper.mapToTool(toolDto));
        final var qrCodeDto = qrCodeMapper.toDto(qrCode);

        return ResponseEntity.ok(qrCodeDto);
    }

    @PostMapping("/decode-qr-code")
    public ResponseEntity<ToolDto> decodeQrCode(@Valid @RequestBody QrCodeDto qrCodeDto) {
        // TODO: Implement decoding
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tools")
    public ResponseEntity<List<ToolDto>> getAllTools() {
        // TODO: Implement getting all tools
        return ResponseEntity.ok().build();
    }
}
