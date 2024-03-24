package es.ivborrezo.shoppinglistmonolith.category;

import java.util.List;

import es.ivborrezo.shoppinglistmonolith.Color.Color;
import es.ivborrezo.shoppinglistmonolith.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long categoryId;
	
	@NotBlank(message = "Name can not be null")
	private String name;
	
	@Enumerated(EnumType.STRING)
	private Color color;
	
	@ManyToMany(mappedBy = "categoryList")
	private List<Product> productList;
}
