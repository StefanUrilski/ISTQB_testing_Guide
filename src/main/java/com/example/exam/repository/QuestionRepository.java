package com.example.exam.repository;

import com.example.exam.domain.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select count(q.questionSet) " +
            "from Question as q " +
            "group by q.questionSet")
    int findAllQuestionSetsNumber();

    @Query(value = "select count(q.id) " +
            "from questions as q " +
            "join questions_users as qu " +
            "on q.id = qu.question_id " +
            "where qu.id = :id", nativeQuery = true)
    int findAllVisitedQuestionsForUser(@Param("id") String id);
}
