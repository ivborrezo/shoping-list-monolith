package es.ivborrezo.shoppinglistmonolith.shoppinglist;

import java.util.Date;
import java.util.List;

import es.ivborrezo.shoppinglistmonolith.product.Product;
import es.ivborrezo.shoppinglistmonolith.user.User;
import lombok.Data;

@Data
public class ShoppingList {

	private Long shoppingListId;
	
	private String name;
	
	private Date creationDate;
	
	private User user;
	
	private List<Product> productList;
}
