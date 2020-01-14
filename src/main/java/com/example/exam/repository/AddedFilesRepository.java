package com.example.exam.repository;

import com.example.exam.domain.entities.AddedFiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddedFilesRepository extends JpaRepository<AddedFiles, Long> {

    AddedFiles findByAddedFileName(String addedFileName);
}
