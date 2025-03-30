package tn.fst.spring.netvoyage.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.entities.MotInterdit;
import tn.fst.spring.netvoyage.services.IMotInterditService;

import java.util.List;

@RestController
@RequestMapping("/api/mots-interdits")
public class MotInterditController {

    @Autowired
    private IMotInterditService motInterditService;

    @GetMapping
    public List<String> getAllMotInterdits() {
        return motInterditService.getAllMotInterdits();
    }

    @PostMapping
    public ResponseEntity<String> addMotInterdit(@RequestBody MotInterdit motInterdit) {
        motInterditService.addMotInterdit(motInterdit);
        return ResponseEntity.ok("Word added successfully");
    }
    @GetMapping("/{word}")
    public ResponseEntity<Boolean> getMotInterdit(@PathVariable String word) {
        boolean exists = motInterditService.findByMot(word).isPresent();
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/{word}")
    public ResponseEntity<String> removeMotInterdit(@PathVariable String word) {
        motInterditService.removeMotInterdit(word);
        return ResponseEntity.ok("Word removed successfully");
    }
}