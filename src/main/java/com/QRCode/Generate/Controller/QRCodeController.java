package com.QRCode.Generate.Controller;

import com.QRCode.Generate.Entity.QrCodes;
import com.QRCode.Generate.Entity.ResponseDto;
import com.QRCode.Generate.Service.QrCodeSerive;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
        try {
            if (carId == null || carId <= 0) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto("error", "Invalid car ID"));
            }
            String url = String.format("https://cartechindia.com/carlist/cardetails/%d", carId);
            byte[] barCodeImage = qrCodeSerive.generateQrCodeImage(url);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename = barcode.png")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(barCodeImage);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("error", "Failed to generate QR code"));
        }
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
