package com.example.qr.domain;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Norpix on 02.05.2025.
 * Description: Class for decoding and reading QR code
 */

@Service
public class ReadQrCodeService { //TODO wtf this class lol

    private String decodeQRCode(File qrCodeimage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);

        if (bufferedImage == null) {
            return null;
        }

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();

        } catch (NotFoundException e) {
            return null;
        }
    }

    private void readQRCode(Scanner scanner) {
        try {
            System.out.print("Enter the path to the QR code file: ");
            String filePath = scanner.nextLine();

            String decodedText = decodeQRCode(new File(filePath));
            if (decodedText == null) {
                System.out.println("Could not read QR code or QR code not found in file.");
            } else {
                System.out.println("Read QR code content: ");
                System.out.println(decodedText);
            }

        } catch (Exception e) {
            System.err.println("An error occurred while reading the QR code: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
