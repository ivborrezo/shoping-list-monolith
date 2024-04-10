package es.ivborrezo.shoppinglistmonolith.product.dto;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.product.Product;

@Service
public class ProductOutputDTOMapper implements Function<Product, ProductOutputDTO> {

	@Override
	public ProductOutputDTO apply(Product product) {
		return new ProductOutputDTO(product.getProductId(), 
				product.getName(), 
				product.getDescription(),
				product.getPrice(), 
				product.getBrand(), 
				product.getGroceryChain());
	}
}
