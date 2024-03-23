package es.ivborrezo.shoppinglistmonolith.user;

import java.util.Date;
import java.util.List;

import es.ivborrezo.shoppinglistmonolith.product.Product;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.ShoppingList;
import lombok.Data;

@Data
public class User {
	
	private Long userId;
	
	private String userName;
	
	private String email;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private Date dateOfBird;
	
	private String phoneNumber;
	
	private List<Product> productList;
	
	private List<ShoppingList> shoppingLists;
}
