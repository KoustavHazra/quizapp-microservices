package com.parker.question_service.service;

import com.parker.question_service.dao.QuestionDao;
import com.parker.question_service.model.Question;
import com.parker.question_service.model.QuestionWrapper;
import com.parker.question_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionDao dao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(dao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.getCause();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        try {
            return new ResponseEntity<>(dao.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.getCause();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            dao.save(question);
            return new ResponseEntity<>("Data stored in db successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            e.getCause();
        }
        return new ResponseEntity<>("Error occurred while storing data in db.", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Integer>> getQuestionForQuiz(String category, Integer numQuestions) {
        List<Integer> questions = dao.getQuestionsByCategory(category, numQuestions);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(List<Integer> questionIds) {
        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        // first from the list of question ids, we need to get all the questions from question table
        for (Integer id: questionIds) {
            questions.add(dao.findById(id).get());
            // as it is an Optional operations, we need to add that .get() too
        }

        // now we have the questions list, we need to get only the wrapped questions (without the correct answer)
        for (Question q: questions) {
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(q.getId());
            wrapper.setQuestionTitle(q.getQuestionTitle());
            wrapper.setOption1(q.getOption1());
            wrapper.setOption2(q.getOption2());
            wrapper.setOption3(q.getOption3());
            wrapper.setOption4(q.getOption4());
            questionsForUser.add(wrapper);
        }
        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);

    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {

        int count = 0;
        for (Response response: responses) {
            // get each response at a time

            Question question = dao.findById(response.getId()).get();
            // using dao, we will get the same question from the question db

            if (response.getResponse().equals(question.getRightAnswer())) {
                // checking the response answer is same with the question right answer
                count++;
            }
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
