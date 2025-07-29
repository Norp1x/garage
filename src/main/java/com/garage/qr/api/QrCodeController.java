package com.garage.qr.api;

import com.example.qr.model.QrCodeDto;
import com.example.qr.model.ToolDto;
import com.garage.qr.domain.GenerateQrImageService;
import com.garage.qr.domain.ToolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/garage")
public class QrCodeController {

    private final GenerateQrImageService generateQrImageService;
    private final ToolMapper toolMapper;
    private final ToolService toolService;
    private final QrCodeMapper qrCodeMapper;

    @PostMapping("/generate-qr-code")
    public ResponseEntity<QrCodeDto> generateQrCode(@Valid @RequestBody ToolDto toolDto) {
        log.info("Generating QR code for tool: {}", toolDto.getName());

        final var qrCode = generateQrImageService.generateQRCode(toolMapper.mapToTool(toolDto));
        final var qrCodeDto = qrCodeMapper.toDto(qrCode);

        return ResponseEntity.ok(qrCodeDto);
    }

    @GetMapping("/generate-qr-code-from-existing-name")
    public ResponseEntity<QrCodeDto> get(@RequestParam String name) {
        final var qrCode = toolService.generateQrCodeForTool(name);
        return ResponseEntity.ok(qrCodeMapper.toDto(qrCode));
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
