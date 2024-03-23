package es.ivborrezo.shoppinglistmonolith.product;

import java.util.List;

import es.ivborrezo.shoppinglistmonolith.category.Category;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.ShoppingList;
import es.ivborrezo.shoppinglistmonolith.user.User;
import lombok.Data;

@Data
public class Product {
	
	private Long productId;
	
	private String name;
	
	private String description;
	
	private double price;
	
	private String brand;
	
	private String groceryChain;
	
	private User user;
	
	private List<ShoppingList> shoppingLists;
	
	private List<Category> categoryList;
	
}
