package es.ivborrezo.shoppinglistmonolith.shoppinglist;

import java.util.Date;
import java.util.List;

import es.ivborrezo.shoppinglistmonolith.listproduct.ListProduct;
import es.ivborrezo.shoppinglistmonolith.user.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
public class ShoppingList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long shoppingListId;
	
	private String name;
	
	private Date creationDate;
	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;
	
	@OneToMany(mappedBy = "shoppingList")
	private List<ListProduct> listProducts;
	
	@ManyToMany(mappedBy = "subscribedShoppingLists")
	private List<User> subscribedUserList;
}
