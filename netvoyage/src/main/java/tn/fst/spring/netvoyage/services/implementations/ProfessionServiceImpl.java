package tn.fst.spring.netvoyage.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.dtos.ProfessionStatsDTO;
import tn.fst.spring.netvoyage.entities.Profession;
import tn.fst.spring.netvoyage.repositories.ProfessionRepository;
import tn.fst.spring.netvoyage.services.interfaces.ProfessionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessionServiceImpl implements ProfessionService {

    private final ProfessionRepository professionRepository;

    @Override
    public List<Profession> getAll() {
        return professionRepository.findAll();
    }

    @Override
    public Optional<Profession> getById(Long id) {
        return professionRepository.findById(id);
    }

    @Override
    public Profession addProfession(Profession profession) {
        return professionRepository.save(profession);
    }

    @Override
    public Profession update(Long id, Profession updatedProfession) {
        return professionRepository.findById(id)
                .map(prof -> {
                    prof.setNom(updatedProfession.getNom());
                    return professionRepository.save(prof);
                })
                .orElseThrow(() -> new RuntimeException("Profession not found"));
    }

    @Override
    public void delete(Long id) {
        professionRepository.deleteById(id);
    }


    @Override
    public List<ProfessionStatsDTO> getStats() {
        // 1. Récupérer toutes les professions depuis la base de données
        List<Profession> professions = professionRepository.findAll();

        // 2. Calculer le nombre total d'employés dans toutes les professions
        int totalEmployes = professions.stream()
                .mapToInt(p -> p.getEmployes().size())
                .sum();

        // 3. Transformer, trier et limiter à 5 meilleurs
        return professions.stream()
                .map(p -> {
                    int nb = p.getEmployes().size();
                    double pct = totalEmployes > 0 ? ((double) nb / totalEmployes) * 100 : 0;
                    return new ProfessionStatsDTO(p.getNom(), nb, pct);
                })
                .sorted((p1, p2) -> Integer.compare(p2.getNombreEmployes(), p1.getNombreEmployes())) // tri décroissant
                .limit(5) // prendre seulement les 5 premiers
                .collect(Collectors.toList());
    }


}
