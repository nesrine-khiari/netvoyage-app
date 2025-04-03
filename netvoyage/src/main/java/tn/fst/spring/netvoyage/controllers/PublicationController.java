package tn.fst.spring.netvoyage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tn.fst.spring.netvoyage.dtos.PublicationDTO;
import tn.fst.spring.netvoyage.entities.Publication;
import tn.fst.spring.netvoyage.services.interfaces.IPublicationService;

import java.util.List;

@RestController
@RequestMapping("/api/publications")
public class PublicationController {
    @Autowired
    private final IPublicationService publicationService;
    public PublicationController(IPublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @PostMapping
    public Publication addPublication(@RequestBody PublicationDTO publication) {
        return publicationService.addPublication(publication);
    }

    @DeleteMapping("/{id}")
    public void deletePublication(@PathVariable Long id) {
         publicationService.deletePublication(id);
    }


    @GetMapping
    public List<Publication> getAllPublications() {
        return publicationService.getAllPublications();
    }
}