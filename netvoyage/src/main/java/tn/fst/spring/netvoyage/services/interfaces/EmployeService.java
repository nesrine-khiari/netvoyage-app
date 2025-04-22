package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.entities.Employe;

import java.util.List;

public interface EmployeService {
    List<Employe> getAllEmployes();
    Employe getEmployeById(Long id);
    Employe createEmploye(Employe employe);
    Employe updateEmploye(Long id, Employe employe);
    void deleteEmploye(Long id);
    long countEmployes();

}