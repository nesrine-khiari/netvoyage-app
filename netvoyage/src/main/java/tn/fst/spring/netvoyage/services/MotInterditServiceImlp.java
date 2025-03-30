package tn.fst.spring.netvoyage.services;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tn.fst.spring.netvoyage.entities.MotInterdit;
import tn.fst.spring.netvoyage.repositories.MotInterditRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MotInterditServiceImlp implements IMotInterditService {
    @Autowired
    private MotInterditRepository motInterditRepository;

    @Override
    public List<String> getAllMotInterdits() {
        return motInterditRepository.findAll().stream().map(MotInterdit::getMot).toList();
    }

    @Override
    public void addMotInterdit(MotInterdit motInterdit) {
        motInterditRepository.save(motInterdit);
    }

    @Override
    @Transactional
    public void removeMotInterdit(String mot) {
        motInterditRepository.deleteByMotIgnoreCase(mot);
    }

    @Override
    public Optional<MotInterdit> findByMot(String mot) {
        return motInterditRepository.findByMotIgnoreCase(mot);
    }
}