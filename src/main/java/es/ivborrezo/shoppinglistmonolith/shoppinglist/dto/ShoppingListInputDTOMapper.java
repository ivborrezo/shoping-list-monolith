package es.ivborrezo.shoppinglistmonolith.shoppinglist.dto;

import java.time.LocalDateTime;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.shoppinglist.ShoppingList;

@Service
public class ShoppingListInputDTOMapper implements Function<ShoppingListInputDTO, ShoppingList> {

	@Override
	public ShoppingList apply(ShoppingListInputDTO shoppingListInputDTO) {

		return ShoppingList.builder().shoppingListId(null).name(shoppingListInputDTO.getName())
				.creationDate(LocalDateTime.now()).build();
	}
}
