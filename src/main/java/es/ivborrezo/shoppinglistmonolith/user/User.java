package es.ivborrezo.shoppinglistmonolith.user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import es.ivborrezo.shoppinglistmonolith.product.Product;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.ShoppingList;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	@NotBlank(message = "UserName can not be null")
	private String userName;

	@NotBlank(message = "Email can not be null")
	@Email(message = "Invalid Email format")
	private String email;

	@NotBlank(message = "Password can not be null")
	private String password;

	@NotBlank(message = "FirstName can not be null")
	private String firstName;

	@NotBlank(message = "LastName can not be null")
	private String lastName;

	@NotBlank(message = "DateOfBirth can not be null")
	@Past(message = "Date of birth must be in the past")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime dateOfBirth;

	@Pattern(regexp = "^$|^(\\+[0-9]{1,3})?[0-9]{8,14}$", message = "Invalid mobile phone format")
	private String phoneNumber;

	@OneToMany(mappedBy = "user")
	private List<Product> productList;

	@OneToMany(mappedBy = "owner")
	private List<ShoppingList> shoppingLists;

	@ManyToMany
	@JoinTable(name = "user_shopping_list_subscription", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "shopping_list_id"))
	private List<ShoppingList> subscribedShoppingLists;
}
