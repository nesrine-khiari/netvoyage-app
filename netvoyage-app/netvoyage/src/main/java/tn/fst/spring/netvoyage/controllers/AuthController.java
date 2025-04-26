package tn.fst.spring.netvoyage.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.fst.spring.netvoyage.dtos.AuthRequest;
import tn.fst.spring.netvoyage.dtos.AuthResponse;
import tn.fst.spring.netvoyage.dtos.RegisterEntrepriseRequest;
import tn.fst.spring.netvoyage.services.interfaces.IAuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/registerEntreprise")
    public ResponseEntity<AuthResponse> registerEntreprise(@RequestBody RegisterEntrepriseRequest req) {
        return ResponseEntity.ok(authService.registerEntreprise(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.authenticate(req));
    }
}
