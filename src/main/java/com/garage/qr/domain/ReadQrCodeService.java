package com.garage.qr.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ReadQrCodeService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Decodes the QR code from the image file
     */
    public Optional<String> decodeQRCode(MultipartFile file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

        if (bufferedImage == null) {
            log.warn("Could not read image from uploaded file");
            return Optional.empty();
        }

        return decodeFromBufferedImage(bufferedImage);
    }

    /**
     * Decodes QR code with Base64 String
     */
    public Optional<String> decodeQRCodeFromBase64(String base64Image) {
        try {
            String imageData = base64Image.replaceFirst("^data:image/[^;]+;base64,", "");
            byte[] imageBytes = Base64.getDecoder().decode(imageData);

            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            return decodeFromBufferedImage(bufferedImage);

        } catch (Exception e) {
            log.error("Error decoding QR code from base64: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Parse Json from the QR code to Tool
     */
    public Optional<Tool> parseToolFromQrCode(String qrCodeContent) {
        try {
            return Optional.of(objectMapper.readValue(qrCodeContent, Tool.class));
        } catch (JsonProcessingException e) {
            log.warn("Could not parse Tool from QR code content: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<String> decodeFromBufferedImage(BufferedImage bufferedImage) {
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            Result result = new MultiFormatReader().decode(bitmap, hints);
            log.info("Successfully decoded QR code");
            return Optional.of(result.getText());

        } catch (NotFoundException e) {
            log.warn("No QR code found in image");
            return Optional.empty();
        }
    }
}
