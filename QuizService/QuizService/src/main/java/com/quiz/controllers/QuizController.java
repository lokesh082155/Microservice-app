package com.quiz.controllers;

import com.quiz.entities.Question;
import com.quiz.entities.Quiz;
import com.quiz.services.QuizService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

	private QuizService quizService;

	public QuizController(QuizService quizService) {
		this.quizService = quizService;
	}

	// create
	@PostMapping
	public Quiz create(@RequestBody Quiz quiz) {
		return quizService.add(quiz);
	}

//    get all

	@GetMapping
	public List<Quiz> get() {
		return quizService.get();
	}

	@GetMapping("/{id}")
	@CircuitBreaker(name = "inventory", fallbackMethod = "fallquizservice")
	public Quiz getOne(@PathVariable Long id) {
		System.err.println("get mapping controller");
		return quizService.get(id);
	}

	public Quiz fallquizservice(Long id, RuntimeException runtimeException) {
		Quiz quiz = quizService.get(id);
		Quiz defaultQuiz = new Quiz();
		defaultQuiz.setId(quiz.getId());
		defaultQuiz.setTitle(quiz.getTitle());
//		defaultQuiz.setQuestions("service is not calling");
		return defaultQuiz;

		// Convert a string to a single-element list of questions
//		List<Question> defaultQuestions = Collections.singletonList("Default Question");

		// If questions field is not yet initialized, initialize it
//		if (defaultQuiz.getQuestions() == null) {
//			defaultQuiz.setQuestions(new ArrayList<>());
//		}
//
//		defaultQuiz.getQuestions().addAll(defaultQuestions);
//		return defaultQuiz;
	}

}
