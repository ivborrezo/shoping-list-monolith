package es.ivborrezo.shoppinglistmonolith.category;

import java.util.List;

import es.ivborrezo.shoppinglistmonolith.product.Product;
import lombok.Data;

@Data
public class Category {
	private long categoryId;
	private String name;
	private String color;
	
	private List<Product> productList;
}
