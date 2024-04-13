package es.ivborrezo.shoppinglistmonolith.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import es.ivborrezo.shoppinglistmonolith.product.Product;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.ShoppingList;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@SequenceGenerator(name = "users_user_id_seq", sequenceName = "users_user_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "users_user_id_seq")
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

	@NotNull(message = "DateOfBirth can not be null")
	@Past(message = "Date of birth must be in the past")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	@Pattern(regexp = "^$|^(\\+[0-9]{1,3})?[0-9]{8,14}$", message = "Invalid mobile phone format")
	private String phoneNumber;

	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> productList = new ArrayList<Product>();

	@Builder.Default
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ShoppingList> shoppingLists = new ArrayList<ShoppingList>();

	@ManyToMany(cascade =
        {
                CascadeType.DETACH,
                CascadeType.MERGE,
                CascadeType.REFRESH,
                CascadeType.PERSIST
        },
        targetEntity = ShoppingList.class)
	@JoinTable(name = "user_shopping_list_subscription", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "shopping_list_id"))
	private List<ShoppingList> subscribedShoppingLists;
	
	public void addProduct(Product product) {
		product.setUser(this);
		productList.add(product);
	}
	
	public void addOwnedShoppingList (ShoppingList shoppingList) {
		shoppingList.setOwner(this);
		shoppingLists.add(shoppingList);
	}
}
