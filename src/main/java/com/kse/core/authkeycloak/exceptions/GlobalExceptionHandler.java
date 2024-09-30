package com.kse.core.authkeycloak.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
																		HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.toList();
		ApiError apiError = ApiError.builder().timestamp(LocalDateTime.now()).error(ex.getMessage()).status(HttpStatus.BAD_REQUEST).errors(errors).build();
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationExceptionException(ConstraintViolationException ex) {
		List<String> errors = ex.getConstraintViolations()
				.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
		ApiError apiError = ApiError.builder().timestamp(LocalDateTime.now()).error(ex.getMessage()).status(HttpStatus.BAD_REQUEST).errors(errors).build();
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ApiError apiError = ApiError.builder().timestamp(LocalDateTime.now()).error(ex.getMessage()).status(HttpStatus.NOT_FOUND).build();
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<Object> handleInternalServerException(InternalServerException ex) {
		ApiError apiError = ApiError.builder().timestamp(LocalDateTime.now()).error(ex.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
		ApiError apiError = ApiError.builder().timestamp(LocalDateTime.now()).error(ex.getMessage()).status(HttpStatus.BAD_REQUEST).build();
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<Object> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
		ApiError apiError = ApiError.builder().timestamp(LocalDateTime.now()).error(ex.getMessage()).status(HttpStatus.CONFLICT).build();
		return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex) {
		ApiError apiError = ApiError.builder().timestamp(LocalDateTime.now()).error(ex.getMessage()).status(HttpStatus.UNAUTHORIZED).build();
		return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
	}
}
