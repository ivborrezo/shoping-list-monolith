package es.ivborrezo.shoppinglistmonolith.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// Handle specific exceptions
	/**
	 * Handles ResourceNotFoundException and returns a custom error message.
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleResourceNotFoundException(
			ResourceNotFoundException resourceNotFoundException, WebRequest webRequest) {

		LOGGER.error("Resource not found: {}.", resourceNotFoundException.getMessage());
		LOGGER.error("Returning status code: {}", HttpStatus.NOT_FOUND.value());

		HttpStatus status = HttpStatus.NOT_FOUND;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				resourceNotFoundException.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);

	}

	/**
	 * Handles UnprocessableEntityException and returns a ResponseEntity with an
	 * appropriate error message.
	 */
	@ExceptionHandler(UnprocessableEntityException.class)
	public ResponseEntity<ErrorMessage> handleUnprocessableEntityException(
			UnprocessableEntityException unprocessableEntityException, WebRequest webRequest) {

		LOGGER.error("Unprocessable Entity Exception occurred: {}", unprocessableEntityException.getMessage());
		LOGGER.error("Returning status code: {}", HttpStatus.UNPROCESSABLE_ENTITY.value());

		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				unprocessableEntityException.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);

	}

	/**
	 * Handles DateTimeParseException and returns a ResponseEntity with an
	 * appropriate error message.
	 */
	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<ErrorMessage> handleDateTimeParseException(DateTimeParseException dateTimeParseException,
			WebRequest webRequest) {

		LOGGER.error("Date Time Parse Exception occurred: {}", dateTimeParseException.getMessage());
		LOGGER.error("Returning status code: {}", HttpStatus.BAD_REQUEST.value());

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				dateTimeParseException.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);
	}

	/**
	 * Handles InvalidFormatException and returns a ResponseEntity with an
	 * appropriate error message.
	 */
	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<ErrorMessage> handleInvalidFormatExceptionException(
			InvalidFormatException invalidFormatException, WebRequest webRequest) {

		LOGGER.error("Invalid Format Exception occurred: {}", invalidFormatException.getMessage());
		LOGGER.error("Returning status code: {}", HttpStatus.BAD_REQUEST.value());

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				invalidFormatException.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);
	}

	/**
	 * Handles HttpMessageNotReadableException and returns a ResponseEntity with an
	 * appropriate error message.
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(
			HttpMessageNotReadableException httpMessageNotReadableException, WebRequest webRequest) {

		LOGGER.error("Http Message NotR eadable Exception occurred: {}", httpMessageNotReadableException.getMessage());
		LOGGER.error("Returning status code: {}", HttpStatus.BAD_REQUEST.value());

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				httpMessageNotReadableException.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);
	}

	/**
	 * Handles MethodArgumentNotValidException by generating a ResponseEntity with
	 * validation error messages.
	 *
	 * This method is responsible for processing MethodArgumentNotValidException,
	 * which occurs when the validation of method arguments annotated with @Valid
	 * fails. It extracts the error details from the exception and constructs a
	 * ValidationErrorMessage containing the error information, such as timestamp,
	 * HTTP status code, reason phrase, error message, request path, and validation
	 * errors. It then returns a ResponseEntity containing the
	 * ValidationErrorMessage and the appropriate HTTP status.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorMessage> handleValidationExceptions(
			MethodArgumentNotValidException methodArgumentNotValidException, WebRequest webRequest) {

		LOGGER.error("Method Argument Not Valid Exception occurred: {}", methodArgumentNotValidException.getMessage());
		LOGGER.error("Returning status code: {}", HttpStatus.UNPROCESSABLE_ENTITY.value());

		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		// Extract validation errors from MethodArgumentNotValidException
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

		// Construct ValidationErrorMessage
		ValidationErrorMessage validationErrorMessage = new ValidationErrorMessage(LocalDateTime.now(), status.value(),
				status.getReasonPhrase(), "Validation Failed", path, validationErrors);

		return new ResponseEntity<>(validationErrorMessage, status);
	}

	/**
	 * Handles global exceptions and returns a generic error message.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleGlobalException(Exception exception, WebRequest webRequest) {

		LOGGER.error("Internal server error: {}", exception.getMessage());
		LOGGER.error("Returning status code: {}", HttpStatus.INTERNAL_SERVER_ERROR.value());

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();

		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				exception.getMessage(), path);

		return new ResponseEntity<>(errorMessage, status);
	}
}
