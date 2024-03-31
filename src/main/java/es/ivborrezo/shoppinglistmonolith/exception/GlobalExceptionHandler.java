package es.ivborrezo.shoppinglistmonolith.exception;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import es.ivborrezo.shoppinglistmonolith.exception.ErrorMessage;
import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Handle specific exceptions
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleResourceNotFoundException(
			ResourceNotFoundException resourceNotFoundException, WebRequest webRequest) {
		HttpStatus notFound = HttpStatus.NOT_FOUND;
		String path = ((ServletWebRequest) webRequest).getRequest().getRequestURI();
		ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), notFound.value(), notFound.getReasonPhrase(),
				resourceNotFoundException.getMessage(), path);

		return new ResponseEntity<>(errorMessage, notFound);

	}
}
