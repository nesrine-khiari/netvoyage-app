package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.dtos.AuthRequest;
import tn.fst.spring.netvoyage.dtos.AuthResponse;
import tn.fst.spring.netvoyage.dtos.RegisterEntrepriseRequest;

public interface IAuthService {
    AuthResponse registerEntreprise(RegisterEntrepriseRequest req);
    AuthResponse authenticate(AuthRequest req);
}
