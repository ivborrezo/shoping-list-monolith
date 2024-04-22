package es.ivborrezo.shoppinglistmonolith.product;

import org.springframework.data.jpa.domain.Specification;

import es.ivborrezo.shoppinglistmonolith.category.Category;
import jakarta.persistence.criteria.Path;

public class ProductSpecifications {
	public static Specification<Product> likeProductName(String productName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")),
				"%" + productName.toLowerCase() + "%");
	}

	public static Specification<Product> likeDescription(String description) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
				"%" + description.toLowerCase() + "%");
	}

	public static Specification<Product> byPriceGreaterThan(double price) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("price"), price);
	}

	public static Specification<Product> byPriceLessThan(double price) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("price"), price);
	}

	public static Specification<Product> likeBrand(String brand) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")),
				"%" + brand.toLowerCase() + "%");
	}

	public static Specification<Product> likeGroceryChain(String groceryChain) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("groceryChain")),
				"%" + groceryChain.toLowerCase() + "%");
	}

	public static Specification<Product> likeCategory(String categoryId) {
		return (root, query, criteriaBuilder) -> {
			final Path<Category> category = root.<Category> get("category");
			return category.in(categoryId);
		};
	}

}
