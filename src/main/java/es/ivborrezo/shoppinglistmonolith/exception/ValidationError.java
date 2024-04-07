package es.ivborrezo.shoppinglistmonolith.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationError {

	private String object;
	private String field;
	private Object rejectedValue;
	private String message;
}
