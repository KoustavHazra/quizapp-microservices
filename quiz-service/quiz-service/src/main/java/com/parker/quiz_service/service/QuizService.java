package com.parker.quiz_service.service;


import com.parker.quiz_service.dao.QuizDao;
import com.parker.quiz_service.feign.QuizInterface;
import com.parker.quiz_service.model.QuestionWrapper;
import com.parker.quiz_service.model.Quiz;
import com.parker.quiz_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    // create different set of quiz tables
    public ResponseEntity<String> createQuiz(String category, Integer numQuestions, String title) {
        List<Integer> questionList = quizInterface.getQuestionForQuiz(category, numQuestions).getBody();
        // using getBody() as the method will return in response entity

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questionList);
        quizDao.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }


    // gives us the quiz tables based on the passed id
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        System.out.println("id is " + id);
        // http://localhost:8765/quiz-service/quiz/getQuiz/1
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Integer> questionIds = quiz.get().getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuizQuestions(questionIds);

        return questions;
    }


    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }
}

/*

Here since we don't have any connection with question db, quiz service have to talk to question
service.
here, we will not be getting the actual questions, instead we will be getting the list of question ids.
Then with another method we can pass those list of questions ids to question-service and
get the wrapper-questions.
And finally we will send the response to the question-service, and get the result.


But how to call a question-service from this quiz-service ????

For example, for the createQuiz() method, we need to call the getQuestionForQuiz() from the question-service.
And we know that each of the method has their own urls, which means if we use call those urls within this code,
that value we can fetch easily.
It's like getting data from the backend to the frontend.

And we can do that using a RestTemplate class, where we can give the localhost url to get data.

---------------------------------------------------------------------------------------

However, we cannot just use the localhost link because it's a microservice, which
means we don't even know which instance of the question-service is available. So better not to hardcode the
port number, neither the url. But then how do we configure it ????

This is where the Feign Client service comes into picture. It is also similar like http web requests,
only difference is it provides us a declarative way of requesting data from one service to another.
So with this we don't need to hardcode the url or port number, adn help us with what APIs we want to
expose or hit from this service to another.

Another thing we need is a service discovery.
Here, the quiz-service is trying to search the question-service, which means the question-service should be
discoverable. To do so, we need some kind of server through which we can find a particular API or microservice,
and that is where the Eureka server is used.

Netflix created this Eureka server.

So how this works is, all the microservices have to register themselves to the Eureka server, and then one
microservice can search another one.
All the service which is registering into Eureka server, is a Eureka client.

So by using this method, we don't need to hard code the ip address or the port number. And with the help of
Feign, we can request from one service to another.

---------------------------------------------------------------------------------------


 */