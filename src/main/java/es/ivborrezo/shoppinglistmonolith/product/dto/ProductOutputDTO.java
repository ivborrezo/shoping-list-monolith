package es.ivborrezo.shoppinglistmonolith.product.dto;

import java.util.List;

import es.ivborrezo.shoppinglistmonolith.category.CategoryOutputDTO;

public record ProductOutputDTO(Long id, String name, String description, double price, String brand,
		String groceryChain, List<CategoryOutputDTO> categoryList) {

}
