package tn.fst.spring.netvoyage.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.DomaineActivite;
import tn.fst.spring.netvoyage.repositories.DomaineActiviteRepository;
import tn.fst.spring.netvoyage.services.interfaces.DomaineActiviteService;

import java.util.List;
import java.util.Optional;

@Service
public class DomaineActiviteServiceImpl implements DomaineActiviteService {

    private final DomaineActiviteRepository domaineActiviteRepository;

    @Autowired
    public DomaineActiviteServiceImpl(DomaineActiviteRepository domaineActiviteRepository) {
        this.domaineActiviteRepository = domaineActiviteRepository;
    }

    @Override
    public DomaineActivite create(DomaineActivite domaine) {
        return domaineActiviteRepository.save(domaine);
    }

    @Override
    public List<DomaineActivite> getAll() {
        return domaineActiviteRepository.findAll();
    }

    @Override
    public Optional<DomaineActivite> getById(Long id) {
        return domaineActiviteRepository.findById(id);
    }

    @Override
    public DomaineActivite update(Long id, DomaineActivite domaine) {
        if (domaineActiviteRepository.existsById(id)) {
            domaine.setId(id);  // Assurez-vous que l'ID correspond à celui de l'entité existante.
            return domaineActiviteRepository.save(domaine);
        }
        throw new RuntimeException("Domaine with id " + id + " not found");
    }

    @Override
    public void delete(Long id) {
        if (domaineActiviteRepository.existsById(id)) {
            domaineActiviteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Domaine with id " + id + " not found");
        }
    }
}
