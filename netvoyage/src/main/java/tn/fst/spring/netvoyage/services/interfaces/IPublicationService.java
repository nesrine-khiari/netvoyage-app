package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.dtos.PublicationDTO;
import tn.fst.spring.netvoyage.entities.Publication;

import java.util.List;
import java.util.Optional;

public interface IPublicationService {
    List<Publication> findAll();
    Publication addPublication(PublicationDTO publicationDTO);
    Optional<Publication> findById(Long id);
    Publication updatePublication(Long id, PublicationDTO publicationDTO);
    void deletePublication(Long id);
}
