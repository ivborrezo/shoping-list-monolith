package es.ivborrezo.shoppinglistmonolith.product;

import org.springframework.data.jpa.domain.Specification;

import es.ivborrezo.shoppinglistmonolith.category.Category;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

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


	public static Specification<Product> byCategoryId(Long categoryId) {
		return (root, query, criteriaBuilder) -> {
			Join<Product, Category> categoryJoin = root.join("categoryList", JoinType.INNER);

			return criteriaBuilder.equal(categoryJoin.get("categoryId"), categoryId);
		};
	}

	public static Specification<Product> likeCategoryName(String categoryName) {
		return (root, query, criteriaBuilder) -> {
			Join<Product, Category> categoryJoin = root.join("categoryList", JoinType.INNER);
			return criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("categoryName")),
					"%" + categoryName.toLowerCase() + "%");
		};
	}
	
	/**
	 * IMPORTANT: It doesn't work correctly if invoked by a specification.and().
	 * Doing nested ands doesn't work. To do that use byCategoryId(Long categoryId)
	 * instead
	 */
//	public static Specification<Product> byCategoryIds(Set<Long> categoryIds) {
//		return (root, query, criteriaBuilder) -> {
//			query.distinct(true); // Ensures that duplicate results are eliminated
//			Join<Product, Category> categoryJoin = root.join("categoryList", JoinType.INNER);
//
//			List<Predicate> predicates = categoryIds.stream()
//					.map(categoryId -> criteriaBuilder.equal(categoryJoin.get("categoryId"), categoryId)).toList();
//
//			// Combine all predicates with AND operation
//			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//		};
//	}

}
