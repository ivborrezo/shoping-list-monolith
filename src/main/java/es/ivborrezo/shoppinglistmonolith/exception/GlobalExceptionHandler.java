package es.ivborrezo.shoppinglistmonolith.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

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
