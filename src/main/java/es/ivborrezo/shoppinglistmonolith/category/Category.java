package es.ivborrezo.shoppinglistmonolith.category;

import java.util.List;

import es.ivborrezo.shoppinglistmonolith.color.Color;
import es.ivborrezo.shoppinglistmonolith.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

	@Id
	@SequenceGenerator(name = "category_category_id_seq", sequenceName = "category_category_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "category_category_id_seq")
	private Long categoryId;

	@NotBlank(message = "Name can not be null")
	private String categoryName;

	@Enumerated(EnumType.STRING)
	private Color color;

	@ManyToMany(mappedBy = "categoryList")
	private List<Product> productList;
}
