package tn.fst.spring.netvoyage.services.interfaces;

public interface InscriptionEmployeService {
    String traiterInscription(String token, String password, String firstname, String lastname, String telephone);

}