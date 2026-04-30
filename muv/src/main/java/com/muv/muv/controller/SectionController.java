package com.muv.muv.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.muv.muv.model.Section;
import com.muv.muv.repository.SectionRepository;
import com.muv.muv.service.SectionService;

@RestController
@RequestMapping("/sections")
@CrossOrigin
public class SectionController {

    private final SectionRepository repo;
    private final SectionService service;

    // 🔥 AQUI É A CORREÇÃO
    public SectionController(SectionRepository repo, SectionService service) {
        this.repo = repo;
        this.service = service;
    }

    @GetMapping
    public List<Section> getByCity(@RequestParam String city) {
        return service.getByCity(city);
    }

    @PostMapping
    public Section create(@RequestBody Section s) {
        return repo.save(s);
    }

    @PutMapping("/{id}")
    public Section update(@PathVariable Long id, @RequestBody Section updated) {
        Section s = repo.findById(id).orElseThrow();
        s.setTitle(updated.getTitle());
        s.setContent(updated.getContent());
        s.setBgColor(updated.getBgColor());
        return repo.save(s);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}