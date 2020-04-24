package com.json.comparator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.json.comparator.exceptions.JsonComparatorExceptions;
import com.json.comparator.exceptions.JsonNotFound;

@ControllerAdvice
public class JsonExceptionController {

	@ExceptionHandler(value = JsonComparatorExceptions.class)
	public ResponseEntity<Object> exception(JsonComparatorExceptions exception) {
		return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
	}
	

	@ExceptionHandler(value = JsonNotFound.class)
	public ResponseEntity<Object> exceptioNot(JsonNotFound exception) {
		return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
	}
	
	
}
