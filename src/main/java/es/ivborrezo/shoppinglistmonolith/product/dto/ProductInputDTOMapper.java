package es.ivborrezo.shoppinglistmonolith.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.category.Category;
import es.ivborrezo.shoppinglistmonolith.product.Product;

@Service
public class ProductInputDTOMapper implements Function<ProductInputDTO, Product> {

	@Override
	public Product apply(ProductInputDTO productInputDTO) {

		List<Category> categoryList = new ArrayList<>();

		if (productInputDTO.getCategoryIds() != null) {
			categoryList = productInputDTO.getCategoryIds().stream()
					.map(categoryId -> 
						Category.builder().categoryId(categoryId).build()
					).collect(Collectors.toList());
		}

		return Product.builder().productId(null).name(productInputDTO.getName())
				.description(productInputDTO.getDescription()).price(productInputDTO.getPrice())
				.brand(productInputDTO.getBrand()).groceryChain(productInputDTO.getGroceryChain())
				.categoryList(categoryList).build();
	}
}
