package com.parker.question_service.dao;

import com.parker.question_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {


    List<Question> findByCategory(String category);

    @Query(value = "SELECT q.id FROM question q " +
                    "WHERE q.category=:category ORDER BY RANDOM() LIMIT :numQuestions ",
                    nativeQuery = true)
    List<Integer> getQuestionsByCategory(String category, Integer numQuestions);
    // since these kinda queries are complex, we need to use sql or native queries to get data
    // here we have written JPQL query
    // and to pass the category and numQ value in the query, we used ":" in front of the variable
    //
}
