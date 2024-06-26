package es.ivborrezo.shoppinglistmonolith.product.dto;

import java.util.HashSet;
import java.util.Set;

import es.ivborrezo.shoppinglistmonolith.validationgroups.BasicValidation;
import es.ivborrezo.shoppinglistmonolith.validationgroups.PatchValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductInputDTO {

	@NotBlank(message = "name can not be empty", groups = BasicValidation.class)
	private String name;

	private String description;

	@Positive(message = "price must be positive", groups = { BasicValidation.class, PatchValidation.class })
	private double price;

	@NotBlank(message = "brand can not be null", groups = BasicValidation.class)
	private String brand;

	private String groceryChain;
	
	@Builder.Default
	private Set<Long> categoryIds = new HashSet<>();

}
