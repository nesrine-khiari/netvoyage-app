package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.dtos.ProfessionStatsDTO;
import tn.fst.spring.netvoyage.entities.Profession;

import java.util.List;
import java.util.Optional;

public interface ProfessionService {
    List<Profession> getAll();
    Optional<Profession> getById(Long id);
    Profession addProfession(Profession profession);
    Profession update(Long id, Profession updatedProfession);
    void delete(Long id);
    List<ProfessionStatsDTO> getStats();

}