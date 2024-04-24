package es.ivborrezo.shoppinglistmonolith.category;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public class CategoryOutputDTOMapper implements Function<Category, CategoryOutputDTO> {

	@Override
	public CategoryOutputDTO apply(Category category) {

		return new CategoryOutputDTO(category.getCategoryId(),
				category.getCategoryName(), 
				category.getColor());
	}
	
	/**
	 * Map to store mappings between Category field names and CategoryOutputDTO field
	 * names that doesn't match. The key is the Category field name. The value is the
	 * CategoryOutputDTO field name.
	 */
	@Getter
	private static final Map<String, String> fieldMappings = new HashMap<>();

	static {
		fieldMappings.put("id", "categoryId");
		fieldMappings.put("name", "categoryName");
	}

}
