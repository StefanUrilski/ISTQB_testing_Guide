package com.example.exam.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "added_files_names")
public class AddedFiles extends BaseEntity {

    private String addedFileName;

    @Column(name = "added_file_name")
    public String getAddedFileName() {
        return addedFileName;
    }

    public void setAddedFileName(String addedFileName) {
        this.addedFileName = addedFileName;
    }
}
