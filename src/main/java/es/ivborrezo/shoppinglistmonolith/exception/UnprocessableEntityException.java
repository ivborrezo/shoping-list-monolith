package es.ivborrezo.shoppinglistmonolith.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnprocessableEntityException extends RuntimeException {

	private String entityName;

	private String fieldName;

	private String fieldValue;

	public UnprocessableEntityException(String message, String entityName, String fieldName, String fieldValue) {
		super(message);
		this.entityName = entityName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

}
