package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.dtos.PublicationDTO;
import tn.fst.spring.netvoyage.entities.Publication;

import java.util.List;

public interface IPublicationService {
    List<Publication> getAllPublications();
    Publication addPublication(PublicationDTO publicationDTO);
    Publication getPublication(Long id);
    Publication updatePublication(Long id, PublicationDTO publicationDTO);
    void deletePublication(Long id);
}
