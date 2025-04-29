package tn.fst.spring.netvoyage.services.implementations;

import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.services.interfaces.IQRCodeService;
import com.google.zxing.*;
import com.google.zxing.common.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeServiceImpl implements IQRCodeService {

    @Override
    public byte[] generateQRCode(String entrepriseInfo) throws Exception {
        int size = 300; // Set QR code size to a larger dimension (300x300)

        // Set options for QR code generation
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1);  // Set margin around the QR code

        // Generate the BitMatrix for the QR code
        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                entrepriseInfo, BarcodeFormat.QR_CODE, size, size, hints
        );

        // Convert BitMatrix to BufferedImage
        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                bufferedImage.setRGB(i, j, bitMatrix.get(i, j) ? 0x000000 : 0xFFFFFF);
            }
        }

        // Convert BufferedImage to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
