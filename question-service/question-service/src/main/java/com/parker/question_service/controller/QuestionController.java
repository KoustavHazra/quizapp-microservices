package com.parker.question_service.controller;


import com.parker.question_service.model.Question;
import com.parker.question_service.model.QuestionWrapper;
import com.parker.question_service.model.Response;
import com.parker.question_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    QuestionService service;

    @Autowired
    Environment environment;

    @GetMapping("allQuestions")
    public ResponseEntity<List<Question>> getAllQuestion() {
        return service.getAllQuestions();
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<Question>> getQuestionByCategory(@PathVariable String category) {
        String lowercaseCategory = category.toLowerCase();
        return service.getQuestionByCategory(lowercaseCategory);
    }

    @PostMapping("addQuestion")
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return service.addQuestion(question);
    }

    // as it is a microservice, quiz and question service will have separate db, and they won't be able
    // to access each other's db. So if quiz needs to generate questions for it, questions will do that
    // job and send it to quiz.
    @GetMapping("generate")
    public ResponseEntity<List<Integer>> getQuestionForQuiz(@RequestParam String category, @RequestParam Integer numQuestions) {
        return service.getQuestionForQuiz(category, numQuestions);
    }


    // also in quiz db only the question ids will be stored while creating a quiz, and so when
    // quiz needs to show those questions based on the id and quiz table we are choosing, those
    // questions will also be provided by the question table.
    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@RequestBody List<Integer> questionIds) {
        System.out.println(environment.getProperty("local.server.port"));
        return service.getQuizQuestions(questionIds);
    }


    // one more thing we need to have it count the total score, because in question db only we are having
    // the correct answers data, thus question service only will do it.
    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses) {
        return service.getScore(responses);
    }
}
