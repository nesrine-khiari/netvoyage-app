package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.entities.MotInterdit;

import java.util.List;
import java.util.Optional;

public interface IMotInterditService {
    List<String> getAllMotInterdits();
    void addMotInterdit(MotInterdit motInterdit);
    void removeMotInterdit(String word);
    Optional<MotInterdit> findByMot(String word);
}
