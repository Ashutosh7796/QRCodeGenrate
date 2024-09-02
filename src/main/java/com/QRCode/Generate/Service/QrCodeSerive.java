package com.QRCode.Generate.Service;

import com.QRCode.Generate.Entity.QrCodes;
import com.QRCode.Generate.Exception.QrCodeGenerationException;
import com.QRCode.Generate.Repository.QrCodesRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QrCodeSerive {

    private final QrCodesRepository qrCodesRepository;

    public byte[] generateQrCodeImage(String text) {
        try {
            MultiFormatWriter barCodeWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = barCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new QrCodeGenerationException("Failed to generate QR code", e);
        }
    }

    @Transactional
    public byte[] generateQrCodeImageByObject(QrCodes qrCodes) {
        try {
            String jsonString = String.format(
                    "{\"id\": %d, \"age\": %d, \"name\": \"%s\"}",
                    qrCodes.getId(), qrCodes.getAge(), qrCodes.getName()
            );

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(jsonString, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
            byte[] qrcodeImage = byteArrayOutputStream.toByteArray();
            qrCodes.setQrCodeImage(qrcodeImage);
            qrCodesRepository.save(qrCodes);
            return qrcodeImage;
        } catch (WriterException | IOException e) {
            throw new QrCodeGenerationException("Failed to generate QR code from object", e);
        }
    }

    public byte[] getQrCodeImageById(int id) {
        Optional<QrCodes> qrCodesOptional = qrCodesRepository.findById(id);
        if (qrCodesOptional.isPresent()) {
            return qrCodesOptional.get().getQrCodeImage();
        } else {
            throw new QrCodeGenerationException("QR code not found with ID: " + id);
        }
    }
}
