package tn.fst.spring.netvoyage.services.interfaces;


import tn.fst.spring.netvoyage.entities.DomaineActivite;

import java.util.List;
import java.util.Optional;

public interface DomaineActiviteService {
    DomaineActivite create(DomaineActivite domaine);
    List<DomaineActivite> getAll();
    Optional<DomaineActivite> getById(Long id);
    DomaineActivite update(Long id, DomaineActivite domaine);
    void delete(Long id);
}