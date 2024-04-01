package es.ivborrezo.shoppinglistmonolith.product;

import java.util.List;

import es.ivborrezo.shoppinglistmonolith.category.Category;
import es.ivborrezo.shoppinglistmonolith.listproduct.ListProduct;
import es.ivborrezo.shoppinglistmonolith.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class Product {

	@Id
	@SequenceGenerator(name = "product_product_id_seq", sequenceName = "product_product_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "product_product_id_seq")
	private Long productId;

	@NotBlank(message = "Name can not be null")
	private String name;

	private String description;

	@Positive(message = "Price must be positive")
	private double price;

	@NotBlank(message = "Brand can not be null")
	private String brand;

	private String groceryChain;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ListProduct> ListProducts;

	@ManyToMany
	@JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<Category> categoryList;

}
