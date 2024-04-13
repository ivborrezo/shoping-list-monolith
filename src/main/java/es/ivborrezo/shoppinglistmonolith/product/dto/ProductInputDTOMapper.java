package es.ivborrezo.shoppinglistmonolith.product.dto;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.product.Product;

@Service
public class ProductInputDTOMapper implements Function<ProductInputDTO, Product> {

	@Override
	public Product apply(ProductInputDTO productInputDTO) {

		return Product.builder().productId(null).name(productInputDTO.getName())
				.description(productInputDTO.getDescription()).price(productInputDTO.getPrice())
				.brand(productInputDTO.getBrand()).groceryChain(productInputDTO.getGroceryChain()).build();
	}
}
