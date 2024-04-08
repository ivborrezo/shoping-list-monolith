package es.ivborrezo.shoppinglistmonolith.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import es.ivborrezo.shoppinglistmonolith.enums.DTOMapping;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Handle specific exceptions
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleResourceNotFoundException(
			ResourceNotFoundException resourceNotFoundException, WebRequest webRequest) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				resourceNotFoundException.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);

	}

	@ExceptionHandler(UnprocessableEntityException.class)
	public ResponseEntity<ErrorMessage> handleUnprocessableEntityException(
			UnprocessableEntityException unprocessableEntityException, WebRequest webRequest) {

		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				unprocessableEntityException.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);

	}

	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<ErrorMessage> handleDateTimeParseException(DateTimeParseException dateTimeParseException,
			WebRequest webRequest) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				dateTimeParseException.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<ErrorMessage> handleInvalidFormatExceptionException(
			InvalidFormatException invalidFormatException, WebRequest webRequest) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				invalidFormatException.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(
			HttpMessageNotReadableException httpMessageNotReadableException, WebRequest webRequest) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				httpMessageNotReadableException.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorMessage> handleValidationExceptions(
			MethodArgumentNotValidException methodArgumentNotValidException, WebRequest webRequest) {

		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		for (ObjectError objectError : methodArgumentNotValidException.getBindingResult().getAllErrors()) {

			FieldError fieldError = null;
			if (objectError instanceof FieldError) {
				fieldError = (FieldError) objectError;
				validationErrors
						.add(new ValidationError(DTOMapping.getModelClassNameFromDtoName(fieldError.getObjectName()),
								fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()));
			} else {
				validationErrors.add(
						new ValidationError(objectError.getObjectName(), null, null, objectError.getDefaultMessage()));
			}

		}

		ValidationErrorMessage validationErrorMessage = new ValidationErrorMessage(LocalDateTime.now(), status.value(),
				status.getReasonPhrase(), "Validation Failed", path, validationErrors);

		return new ResponseEntity<>(validationErrorMessage, status);
	}

	// Handle global exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleGlobalException(Exception exception, WebRequest webRequest) {

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				exception.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);
	}
}
