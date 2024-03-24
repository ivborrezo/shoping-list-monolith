package es.ivborrezo.shoppinglistmonolith.user;

import java.util.Date;
import java.util.List;

import es.ivborrezo.shoppinglistmonolith.product.Product;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.ShoppingList;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	
	private String userName;
	
	private String email;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private Date dateOfBird;
	
	private String phoneNumber;
	
	@OneToMany(mappedBy = "user")
	private List<Product> productList;
	
	@OneToMany(mappedBy = "owner")
	private List<ShoppingList> shoppingLists;
	
	@ManyToMany
	@JoinTable(
			name = "user_shopping_list_subscription",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "shopping_list_id")
	)
	private List<ShoppingList> subscribedShoppingLists;
}
