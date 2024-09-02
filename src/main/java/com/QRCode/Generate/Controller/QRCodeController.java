package com.QRCode.Generate.Controller;

import com.QRCode.Generate.Entity.QrCodes;
import com.QRCode.Generate.Service.QrCodeSerive;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/QrCodes")
@RequiredArgsConstructor
public class QRCodeController {

    private final QrCodeSerive qrCodeSerive;

    @GetMapping("/GenerateQrCode")
    public ResponseEntity<?> generateQrCodeByText(@RequestParam Long carId) throws IOException, WriterException {
        String url = String.format("https://cartechindia.com/carlist/cardetails/%d", carId);
        byte[] barCodeImage = qrCodeSerive.generateQrCodeImage(url);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename = barcode.png")
                .contentType(MediaType.IMAGE_PNG)
                .body(barCodeImage);
    }

    @PostMapping("/GenerateQrByObject")
    public ResponseEntity<?> generateQrCodeByObject (@RequestBody QrCodes qrCodes) throws IOException, WriterException {
        try {
            byte[] barCodeImage = qrCodeSerive.generateQrCodeImageByObject(qrCodes);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename = barcode.png")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(barCodeImage);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        }
    @GetMapping("/GetQrCode/{id}")
    public ResponseEntity<?> getQrCodeById(@PathVariable int id) {
        byte[] qrCodeImage = qrCodeSerive.getQrCodeImageById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=qr_code.png")
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeImage);
    }
}
