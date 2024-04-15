package es.ivborrezo.shoppinglistmonolith.shoppinglist.dto;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.shoppinglist.ShoppingList;

@Service
public class ShoppingListOutputDTOMapper implements Function<ShoppingList, ShoppingListOutputDTO> {

	@Override
	public ShoppingListOutputDTO apply(ShoppingList ShoppingList) {
		return new ShoppingListOutputDTO(ShoppingList.getShoppingListId(), ShoppingList.getName(), ShoppingList.getCreationDate());
	}
}
