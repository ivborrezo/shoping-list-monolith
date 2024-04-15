package es.ivborrezo.shoppinglistmonolith.shoppinglist;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import es.ivborrezo.shoppinglistmonolith.listproduct.ListProduct;
import es.ivborrezo.shoppinglistmonolith.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingList {

	@Id
	@SequenceGenerator(name = "shopping_list_shopping_list_id_seq", sequenceName = "shopping_list_shopping_list_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "shopping_list_shopping_list_id_seq")
	private Long shoppingListId;

	@NotEmpty(message = "Name can not be null")
	private String name;

	@NotNull(message = "CreationDate can not be null")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime creationDate;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ListProduct> listProducts;

//	@ManyToMany(mappedBy = "subscribedShoppingLists")
	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH,
			CascadeType.PERSIST }, targetEntity = User.class)
	@JoinTable(name = "user_shopping_list_subscription", joinColumns = @JoinColumn(name = "shopping_list_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> subscribedUserList;
}
