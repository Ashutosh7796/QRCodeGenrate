package com.QRCode.Generate.Repository;

import com.QRCode.Generate.Entity.QrCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QrCodesRepository extends JpaRepository<QrCodes, Integer> {
}
