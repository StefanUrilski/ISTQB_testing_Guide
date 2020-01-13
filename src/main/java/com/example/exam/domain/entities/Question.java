package com.example.exam.domain.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "questions")
public class Question extends BaseEntity {

    private String question;
    private Set<Answer> answers;
    private char correctAnswer;
    private String explaining;

    public Question() {
        answers = new LinkedHashSet<>();
    }

    @Column(columnDefinition = "text")
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @OneToMany(targetEntity = Answer.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    @Column(name = "correct_answer")
    public char getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(char correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Column(columnDefinition = "text")
    public String getExplaining() {
        return explaining;
    }

    public void setExplaining(String explaining) {
        this.explaining = explaining;
    }
}
