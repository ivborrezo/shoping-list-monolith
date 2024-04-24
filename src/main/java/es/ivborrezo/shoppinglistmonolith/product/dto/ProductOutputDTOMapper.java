package es.ivborrezo.shoppinglistmonolith.product.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.category.CategoryOutputDTO;
import es.ivborrezo.shoppinglistmonolith.category.CategoryOutputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.product.Product;
import lombok.Getter;

@Service
public class ProductOutputDTOMapper implements Function<Product, ProductOutputDTO> {

	private CategoryOutputDTOMapper categoryOutputDTOMapper;
	
	public ProductOutputDTOMapper(CategoryOutputDTOMapper categoryOutputDTOMapper) {
		this.categoryOutputDTOMapper = categoryOutputDTOMapper;
	}
	
	@Override
	public ProductOutputDTO apply(Product product) {
		
		List<CategoryOutputDTO> listCategoryOutputDTO = product.getCategoryList().stream().map(categoryOutputDTOMapper)
				.collect(Collectors.toList());
		
		return new ProductOutputDTO(product.getProductId(), 
				product.getProductName(), 
				product.getDescription(),
				product.getPrice(), 
				product.getBrand(), 
				product.getGroceryChain(),
				listCategoryOutputDTO);
		
	}
	
	/**
	 * Map to store mappings between Product field names and ProductOutputDTO field
	 * names that doesn't match. The key is the Product field name. The value is the
	 * ProductOutputDTO field name.
	 */
	@Getter
	private static final Map<String, String> fieldMappings = new HashMap<>();

	static {
		fieldMappings.put("id", "productId");
		fieldMappings.put("name", "productName");
	}
}
