package com.example.exam.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "answers")
public class Answer extends BaseEntity {

    private char symbol;
    private String description;

    public Answer() {
    }

    public Answer(char symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }

    @Column
    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    @Column(columnDefinition = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
