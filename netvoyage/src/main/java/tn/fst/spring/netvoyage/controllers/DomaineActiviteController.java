package tn.fst.spring.netvoyage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.entities.DomaineActivite;
import tn.fst.spring.netvoyage.services.interfaces.DomaineActiviteService;

import java.util.List;

@RestController
@RequestMapping("/api/domaines")
@RequiredArgsConstructor
public class DomaineActiviteController {

    private final DomaineActiviteService domaineService;

    @PostMapping
    public ResponseEntity<DomaineActivite> create(@RequestBody DomaineActivite domaine) {
        return ResponseEntity.ok(domaineService.create(domaine));
    }

    @GetMapping
    public ResponseEntity<List<DomaineActivite>> getAll() {
        return ResponseEntity.ok(domaineService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DomaineActivite> getById(@PathVariable Long id) {
        return domaineService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DomaineActivite> update(@PathVariable Long id, @RequestBody DomaineActivite domaine) {
        return ResponseEntity.ok(domaineService.update(id, domaine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        domaineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
