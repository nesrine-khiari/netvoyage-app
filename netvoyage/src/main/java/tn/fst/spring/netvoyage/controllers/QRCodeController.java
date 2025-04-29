package tn.fst.spring.netvoyage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tn.fst.spring.netvoyage.entities.Entreprise;
import tn.fst.spring.netvoyage.repositories.EntrepriseRepository;
import tn.fst.spring.netvoyage.services.interfaces.IQRCodeService;

import java.util.Optional;

@RestController
public class QRCodeController {

    @Autowired
    private IQRCodeService qrCodeService;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @GetMapping("/generateQRCode/{id}")
    public ResponseEntity<byte[]> generateQRCode(@PathVariable Long id) {
        try {
            // Retrieve the enterprise based on the given ID
            Optional<Entreprise> entrepriseOpt = entrepriseRepository.findById(id);
            if (entrepriseOpt.isPresent()) {
                Entreprise entreprise = entrepriseOpt.get();

                // Create entreprise info in vCard-like format
                String entrepriseInfo = "BEGIN:VCARD\n" +
                        "VERSION:3.0\n" +
                        "FN:" + entreprise.getNomEntreprise() + "\n" +
                        "ADR:" + entreprise.getAdresse() + ", " + entreprise.getPays() + "\n" +
                        "TEL:" + entreprise.getTelephone() + "\n" +
                        "Email:" + entreprise.getUser().getEmail() + "\n" +
                        "END:VCARD";

                // Generate the QR code
                byte[] qrCodeBytes = qrCodeService.generateQRCode(entrepriseInfo);

                // Set headers for image content type
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "image/png");

                // Return the image as a byte array with a proper header
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(qrCodeBytes);
            } else {
                // Return a "not found" response if entreprise is not present
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
        } catch (Exception e) {
            // Return internal server error if there was an issue generating the QR code
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
