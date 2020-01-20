package com.example.exam.repository;

import com.example.exam.domain.entities.Figure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FigureRepository extends JpaRepository<Figure, Long> {

}
