package tn.fst.spring.netvoyage.services.implementations;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.dtos.PublicationDTO;
import tn.fst.spring.netvoyage.entities.Publication;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.repositories.PublicationRepository;
import tn.fst.spring.netvoyage.services.interfaces.IPublicationService;
import tn.fst.spring.netvoyage.services.interfaces.IUserService;

import java.util.List;
import java.util.Optional;

@Service
public class PublicationServiceImpl implements IPublicationService {
    @Autowired
    private  PublicationRepository publicationRepository;

    @Autowired
    private IUserService userService;

    @Override
    public List<Publication> findAll() {
        return publicationRepository.findAll();
    }

    @Override
    public Publication addPublication(PublicationDTO publicationDTO) {
        if (publicationDTO == null) {
            throw new IllegalArgumentException("Publication data cannot be null");
        }

        // Fetch user safely
        User user = userService.getUserById(publicationDTO.getNumOwner());
        if (user == null) {
            throw new RuntimeException("User with ID " + publicationDTO.getNumOwner() + " not found");
        }

        // Create and populate the publication
        Publication publication = new Publication();
        publication.setContent(publicationDTO.getContent());
        publication.setTitle(publicationDTO.getTitle());
        publication.setOwner(user);

        // Save and return the publication
        return publicationRepository.save(publication);
    }


    @Override
    public Optional<Publication> findById(Long id) {
        return publicationRepository.findById(id);
    }

    @Override
    public Publication updatePublication(Long id, PublicationDTO publicationDTO) {
        Optional<Publication> optionalPublication = findById(id);

        if (optionalPublication.isPresent()) {
            Publication pub = optionalPublication.get();
            pub.setContent(publicationDTO.getContent());
            pub.setTitle(publicationDTO.getTitle());
            return publicationRepository.save(pub);
        }

        throw new EntityNotFoundException("Publication with ID " + id + " not found");
    }


    @Override
    public void deletePublication(Long id) {
         publicationRepository.deleteById(id);
    }
}
