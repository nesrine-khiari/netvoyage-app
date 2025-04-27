package tn.fst.spring.netvoyage.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.fst.spring.netvoyage.dtos.AuthRequest;
import tn.fst.spring.netvoyage.dtos.AuthResponse;
import tn.fst.spring.netvoyage.dtos.RegisterEntrepriseRequest;
import tn.fst.spring.netvoyage.services.interfaces.IAuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/registerEntreprise")
    public ResponseEntity<?> registerEntreprise(@RequestBody RegisterEntrepriseRequest req) {
        AuthResponse response = authService.registerEntreprise(req);

        if (response == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User already exists with this email");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.authenticate(req));
    }
}
