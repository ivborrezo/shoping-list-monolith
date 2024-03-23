package es.ivborrezo.shoppinglistmonolith.listproduct;

import es.ivborrezo.shoppinglistmonolith.product.Product;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.ShoppingList;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;

@Entity
@Data
public class ListProduct {

	@EmbeddedId
	private ListProductKey listProductKey;
	
	@ManyToOne
	@MapsId("shoppingListId")
	@JoinColumn(name = "shopping_list_id")
	private ShoppingList shoppingList;
	
	@ManyToOne
	@MapsId("productId")
	@JoinColumn(name = "product_id")
	private Product product;
	
	private int quantity;
}
