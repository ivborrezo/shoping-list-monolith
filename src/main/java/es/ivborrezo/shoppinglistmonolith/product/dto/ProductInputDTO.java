package es.ivborrezo.shoppinglistmonolith.product.dto;

import java.util.ArrayList;
import java.util.List;

import es.ivborrezo.shoppinglistmonolith.validationgroups.BasicValidation;
import es.ivborrezo.shoppinglistmonolith.validationgroups.PatchValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
	private List<Long> categoryIds = new ArrayList<Long>();

}
