package com.QRCode.Generate.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
public class QrCodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the ID
    private int id;

    public int Age;

    public String Name;

    @Lob
    @Column(name = "qr_code_image", columnDefinition = "BLOB")
    private byte[] qrCodeImage;
}
