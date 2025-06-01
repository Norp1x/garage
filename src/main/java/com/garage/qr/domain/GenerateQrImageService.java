package com.garage.qr.domain;

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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Norpix on 02.05.2025.
 * Description: Class for generating QR codes
 */
@Slf4j
@Service
public class GenerateQrImageService {

    private static final int DEFAULT_SIZE = 250;
    private static final String QR_CODES_DIRECTORY = "./QrCodes/";
    private static final String IMAGE_EXTENSION_TYPE = ".png";
    private static final String QR_CODE_WAS_GENERATED_AND_SAVED_INFO_LOG = "QR code was generated and saved as: {}";
    private static final String AN_ERROR_OCCURRED_WHEN_TRIED_TO_GENERATE_QR_CODE_ERROR_LOG = "An error occurred when tried to generate QR Code: {}";
    private static final String ERROR_MESSAGE = "Error generating QR code: ";
    private static final String LABEL_FONT_NAME = "Arial";
    private static final String IMAGE_FORMAT_NAME = "PNG";
    private static final String DATA_URI_SCHEME_PREFIX = "data:image/png;base64,";
    private static final String COULD_NOT_READ_TOOL_NAME_WARN_LOG = "Could not read tool name from JSON: {}";
    private static final String EXAMPLE_TOOL_NAME = "Example Tool";
    private static final String COULD_NOT_READ_TOOL_SIZE_WARN_LOG = "Could not read tool size from JSON: {}";
    private static final String EXAMPLE_TOOL_SIZE = "Small/Medium/Large";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QrCode generateQRCode(Tool tool) {
        try {
            Path qrCodesDirectoryPath = Paths.get(QR_CODES_DIRECTORY);
            Files.createDirectories(qrCodesDirectoryPath);
            String fileName = tool.name() + IMAGE_EXTENSION_TYPE;
            Path qrCodePath = qrCodesDirectoryPath.resolve(fileName);

            String toolJson = convertToolToJson(tool);

            String qrCodeBase64 = generateQRCodeImage(toolJson, qrCodePath);
            log.info(QR_CODE_WAS_GENERATED_AND_SAVED_INFO_LOG, qrCodePath);

            return QrCode.builder()
                    .name(fileName)
                    .image(qrCodeBase64)
                    .build();

        } catch (Exception exception) {
            log.error(AN_ERROR_OCCURRED_WHEN_TRIED_TO_GENERATE_QR_CODE_ERROR_LOG, exception.getMessage(), exception);
            throw new RuntimeException(ERROR_MESSAGE + exception.getMessage(), exception);
        }
    }

    private String convertToolToJson(Tool tool) throws JsonProcessingException {
        return objectMapper.writeValueAsString(tool);
    }

    private String generateQRCodeImage(String text, Path filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
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
        graphics.setFont(new Font(LABEL_FONT_NAME, Font.BOLD, 18));
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

        ImageIO.write(combined, IMAGE_FORMAT_NAME, filePath.toFile());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(combined, IMAGE_FORMAT_NAME, byteArrayOutputStream);

        return DATA_URI_SCHEME_PREFIX + Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    private String extractToolNameFromJson(String json) {
        try {
            Tool tool = objectMapper.readValue(json, Tool.class);
            return tool.name() != null ? tool.name() : StringUtils.EMPTY;
        } catch (JsonProcessingException e) {
            log.warn(COULD_NOT_READ_TOOL_NAME_WARN_LOG, e.getMessage());
            return EXAMPLE_TOOL_NAME;
        }
    }

    private String extractToolSizeFromJson(String json) {
        try {
            Tool tool = objectMapper.readValue(json, Tool.class);
            return tool.size() != null ? tool.size() : StringUtils.EMPTY;
        } catch (JsonProcessingException e) {
            log.warn(COULD_NOT_READ_TOOL_SIZE_WARN_LOG, e.getMessage());
            return EXAMPLE_TOOL_SIZE;
        }
    }

}
