package es.ivborrezo.shoppinglistmonolith.shoppinglist.dto;

import java.time.LocalDateTime;

public record ShoppingListOutputDTO(Long id, String name, LocalDateTime creationDate) {

}
