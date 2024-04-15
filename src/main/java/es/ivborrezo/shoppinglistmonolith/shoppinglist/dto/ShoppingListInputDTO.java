package es.ivborrezo.shoppinglistmonolith.shoppinglist.dto;

import es.ivborrezo.shoppinglistmonolith.validationgroups.BasicValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListInputDTO {

	@NotBlank(message = "name can not be empty", groups = BasicValidation.class)
	private String name;

}
