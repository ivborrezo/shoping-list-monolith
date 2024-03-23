package es.ivborrezo.shoppinglistmonolith.category;

import java.util.List;

import es.ivborrezo.shoppinglistmonolith.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "category")
@Data
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long categoryId;
	
	private String name;
	
	private String color;
	
	@ManyToMany(mappedBy = "categoryList")
	private List<Product> productList;
}
