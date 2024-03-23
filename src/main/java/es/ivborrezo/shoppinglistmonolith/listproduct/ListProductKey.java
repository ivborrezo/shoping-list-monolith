package es.ivborrezo.shoppinglistmonolith.listproduct;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ListProductKey implements Serializable {

	@Column(name = "shopping_list_id")
	private Long shoppingListId;
	
	@Column(name = "product_id")
	private Long productId;
	
}
