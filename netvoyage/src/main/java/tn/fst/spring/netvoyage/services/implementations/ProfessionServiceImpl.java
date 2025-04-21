package tn.fst.spring.netvoyage.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.Profession;
import tn.fst.spring.netvoyage.repositories.ProfessionRepository;
import tn.fst.spring.netvoyage.services.interfaces.ProfessionService;

import java.util.List;
import java.util.Optional;

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
}
