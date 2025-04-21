package tn.fst.spring.netvoyage.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.Employe;
import tn.fst.spring.netvoyage.entities.Entreprise;
import tn.fst.spring.netvoyage.entities.Profession;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.repositories.EmployeRepository;
import tn.fst.spring.netvoyage.repositories.EntrepriseRepository;
import tn.fst.spring.netvoyage.repositories.ProfessionRepository;
import tn.fst.spring.netvoyage.repositories.UserRepository;
import tn.fst.spring.netvoyage.services.interfaces.EmployeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements EmployeService {

    private final EmployeRepository employeRepository;
    private final ProfessionRepository professionRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final UserRepository userRepository;


    @Override
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    @Override
    public Employe getEmployeById(Long id) {
        return employeRepository.findById(id).orElse(null);
    }

    @Override
    public Employe createEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    @Override
    public Employe updateEmploye(Long id, Employe employe) {
        Employe existing = employeRepository.findById(id).orElse(null);
        if (existing != null) {
            if (employe.getFirstname() != null)
                existing.setFirstname(employe.getFirstname());

            if (employe.getLastname() != null)
                existing.setLastname(employe.getLastname());

            if (employe.getAdresse() != null)
                existing.setAdresse(employe.getAdresse());

            if (employe.getTelephone() != null)
                existing.setTelephone(employe.getTelephone());

            if (employe.getProfession() != null && employe.getProfession().getId() != null) {
                Profession existingProfession = professionRepository.findById(employe.getProfession().getId())
                        .orElseThrow(() -> new RuntimeException("Profession not found"));
                existing.setProfession(existingProfession);
            }

            if (employe.getEntreprise() != null && employe.getEntreprise().getId() != null) {
                Entreprise existingEntreprise = entrepriseRepository.findById(employe.getEntreprise().getId())
                        .orElseThrow(() -> new RuntimeException("Entreprise not found"));
                existing.setEntreprise(existingEntreprise);
            }

            if (employe.getUser() != null && employe.getUser().getNumUser() != null) {
                User existingUser = userRepository.findById(employe.getUser().getNumUser())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                existing.setUser(existingUser);
            }

            return employeRepository.save(existing);
        }
        return null;
    }


    @Override
    public void deleteEmploye(Long id) {
        employeRepository.deleteById(id);
    }
}