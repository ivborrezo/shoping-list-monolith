package es.ivborrezo.shoppinglistmonolith.product.dto;

public record ProductOutputDTO(Long id, String name, String Description, double price, String brand,
		String groceryChain) {

}
