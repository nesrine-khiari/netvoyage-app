package tn.fst.spring.netvoyage.services.interfaces;

public interface IQRCodeService {

    /**
     * Generate a QR code based on the given enterprise information.
     *
     * @param entrepriseInfo Information to encode in the QR code.
     * @return byte array representing the QR code image.
     * @throws Exception if QR code generation fails.
     */
    byte[] generateQRCode(String entrepriseInfo) throws Exception;
}
