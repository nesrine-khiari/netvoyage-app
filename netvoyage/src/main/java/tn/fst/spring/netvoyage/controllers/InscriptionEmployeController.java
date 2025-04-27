package tn.fst.spring.netvoyage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.services.interfaces.InscriptionEmployeService;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inscription")
public class InscriptionEmployeController {


    private final InscriptionEmployeService inscriptionService;

    @PostMapping
    public ResponseEntity<String> inscrireEmployeViaToken(
            @RequestParam String token,
            @RequestBody Map<String, String> data) {

        String password = data.get("password");
        String firstname = data.get("firstname");
        String lastname = data.get("lastname");
        String telephone = data.get("telephone");

        String result = inscriptionService.traiterInscription(token, password, firstname, lastname, telephone);
        return ResponseEntity.ok(result);
    }
}
