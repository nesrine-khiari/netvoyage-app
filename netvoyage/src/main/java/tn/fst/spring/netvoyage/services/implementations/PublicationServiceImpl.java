package tn.fst.spring.netvoyage.services.implementations;

import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.dtos.PublicationDTO;
import tn.fst.spring.netvoyage.entities.Publication;
import tn.fst.spring.netvoyage.repositories.PublicationRepository;
import tn.fst.spring.netvoyage.services.interfaces.IPublicationService;

import java.util.List;

@Service
public class PublicationServiceImpl implements IPublicationService {
    private final PublicationRepository publicationRepository;
    public PublicationServiceImpl(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }
    public List<Publication> getAllPublications() {
        return publicationRepository.findAll();
    }

    @Override
    public Publication addPublication(PublicationDTO publicationDTO) {
        Publication publication = new Publication();
        publication.setContent(publicationDTO.getContent());
        publication.setTitle(publicationDTO.getTitle());

        return publicationRepository.save(publication);
    }

    @Override
    public Publication getPublication(Long id) {
        return publicationRepository.findById(id).orElse(null);
    }

    @Override
    public Publication updatePublication(Long id, PublicationDTO publicationDTO) {
        Publication publication = getPublication(id);
        publication.setContent(publicationDTO.getContent());
        publication.setTitle(publicationDTO.getTitle());
        return publicationRepository.save(publication);
    }

    @Override
    public void deletePublication(Long id) {
         publicationRepository.deleteById(id);
    }
}
