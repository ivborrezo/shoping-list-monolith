package es.ivborrezo.shoppinglistmonolith.product;

import java.util.List;

import es.ivborrezo.shoppinglistmonolith.category.Category;
import es.ivborrezo.shoppinglistmonolith.listproduct.ListProduct;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.ShoppingList;
import es.ivborrezo.shoppinglistmonolith.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "product")
@Data
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long productId;

	private String name;

	private String description;

	private double price;

	private String brand;

	private String groceryChain;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "product")
	private List<ListProduct> ListProducts;

	@ManyToMany
	@JoinTable(
			name = "product_category", 
			joinColumns = @JoinColumn(name = "product_id"), 
			inverseJoinColumns = @JoinColumn(name = "category_id")
	)
	private List<Category> categoryList;

}
