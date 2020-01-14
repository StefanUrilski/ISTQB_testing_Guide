package com.example.exam.repository;

import com.example.exam.domain.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select count(q.questionSet) " +
            "from Question as q " +
            "group by q.questionSet")
    int findAllQuestionSetsNumber();

    @Query("select count(q.id) " +
            "from Question as q " +
            "where q.users.name = :username")
    int findAllVisitedQuestionsForUser(String username);
}
