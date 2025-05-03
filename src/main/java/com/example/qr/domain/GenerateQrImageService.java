package com.example.qr.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Norpix on 02.05.2025.
 * Description: Class for generating QR codes
 */
@Slf4j
@Service
public class GenerateQrImageService { //TODO move all Strings to statics

    private static final int DEFAULT_SIZE = 250;
    private static final String DEFAULT_CHARSET = "UTF-8";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QrCode generateQRCode(Tool tool) {
        try {
            String fileName = tool.getName();
            String filePath = "./" + fileName + ".png";

            String toolJson = convertToolToJson(tool);

            String qrCodeBase64 = generateQRCodeImage(toolJson, filePath);
            log.info("QR code was generated and saved as: {}", filePath);

            return QrCode.builder()
                    .name(fileName)
                    .image(qrCodeBase64)
                    .build();

        } catch (Exception e) {
            log.error("An error occurred when tried to generate QR Code: {}", e.getMessage(), e);
            throw new RuntimeException("Error generating QR code: " + e.getMessage(), e);
        }
    }

    private String convertToolToJson(Tool tool) throws JsonProcessingException {
        return objectMapper.writeValueAsString(tool);
    }

    private String generateQRCodeImage(String text, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, DEFAULT_SIZE, DEFAULT_SIZE, hints);

        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        int labelHeight = 70;
        BufferedImage combined = new BufferedImage(DEFAULT_SIZE, DEFAULT_SIZE + labelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = combined.createGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, combined.getWidth(), combined.getHeight());

        graphics.drawImage(qrImage, 0, 0, null);

        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fontMetrics = graphics.getFontMetrics();
        String toolName = extractToolNameFromJson(text);
        int textWidthName = fontMetrics.stringWidth(toolName);
        int xName = (DEFAULT_SIZE - textWidthName) / 2;
        int textYStart = DEFAULT_SIZE + (labelHeight - fontMetrics.getHeight()) / 4;
        graphics.drawString(toolName, xName, textYStart + fontMetrics.getAscent());

        String toolSize = extractToolSizeFromJson(text);
        int textWidthSize = fontMetrics.stringWidth(toolSize);
        int xSize = (DEFAULT_SIZE - textWidthSize) / 2;
        graphics.drawString(toolSize, xSize, textYStart + fontMetrics.getAscent() + fontMetrics.getHeight());

        graphics.dispose();

        Path path = FileSystems.getDefault().getPath(filePath);
        ImageIO.write(combined, "PNG", path.toFile());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(combined, "PNG", byteArrayOutputStream);

        return "data:image/png;base64," + Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    private String extractToolNameFromJson(String json) {
        try {
            Tool tool = objectMapper.readValue(json, Tool.class);
            return tool.getName() != null ? tool.getName() : StringUtils.EMPTY;
        } catch (JsonProcessingException e) {
            log.warn("Could not read tool name from JSON: {}", e.getMessage());
            return "Example Tool";
        }
    }

    private String extractToolSizeFromJson(String json) {
        try {
            Tool tool = objectMapper.readValue(json, Tool.class);
            return tool.getSize() != null ? tool.getSize() : StringUtils.EMPTY;
        } catch (JsonProcessingException e) {
            log.warn("Could not read tool size from JSON: {}", e.getMessage());
            return "Example Tool";
        }
    }

}
