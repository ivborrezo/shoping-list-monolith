package es.ivborrezo.shoppinglistmonolith.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Getter
public class ValidationErrorMessage extends ErrorMessage {

	private List<ValidationError> details;

	ValidationErrorMessage(LocalDateTime timestamp, int status, String error, String message, String path, List<ValidationError> details) {
		super(timestamp, status, error, message, path);
		this.details = details;
	}
}
