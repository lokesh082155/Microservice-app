package com.quiz.services.impl;

import com.quiz.entities.Emailevent;
import com.quiz.entities.Quiz;
import com.quiz.repositories.QuizRepository;
import com.quiz.services.QuestionClient;
import com.quiz.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {
	private final QuizRepository quizRepository;
	private final QuestionClient questionClient;
//    private final KafkaTemplate<String, Emailevent> kafkaTemplate;

	@Autowired
	public QuizServiceImpl(QuizRepository quizRepository, QuestionClient questionClient,
			KafkaTemplate<String, Emailevent> kafkaTemplate) {
		this.quizRepository = quizRepository;
		this.questionClient = questionClient;
//        this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public Quiz add(Quiz quiz) {
		return quizRepository.save(quiz);
	}

	@Override
	public List<Quiz> get() {
		List<Quiz> quizzes = quizRepository.findAll();

		List<Quiz> newQuizList = quizzes.stream().map(quiz -> {
			quiz.setQuestions(questionClient.getQuestionOfQuiz(quiz.getId()));
			return quiz;
		}).collect(Collectors.toList());

		return newQuizList;
	}

	@Override
	public Quiz get(Long id) {
		System.err.println("service layer quiz before calling");
		Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
		System.err.println(quiz.getTitle());
		quiz.setQuestions(questionClient.getQuestionOfQuiz(quiz.getId()));
//        kafkaTemplate.send("notificationTopic", new Emailevent(quiz.getTitle()));
		System.err.println("service layer quiz after calling");
		return quiz;
	}
}
