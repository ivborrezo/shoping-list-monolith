package es.ivborrezo.shoppinglistmonolith.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {
	private LocalDateTime timestamp;
	private int status;
	private String error;
	private String message;
	private String path;
	
}
