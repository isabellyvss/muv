package com.muv.muv.service;

import com.muv.muv.model.Section;
import com.muv.muv.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {

    private final SectionRepository repo;

    public SectionService(SectionRepository repo) {
        this.repo = repo;
    }

    public List<Section> getByCity(String city) {
        return repo.findByCity(city);
    }
}