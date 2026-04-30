package com.muv.muv.repository;

import com.muv.muv.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCity(String city);
}