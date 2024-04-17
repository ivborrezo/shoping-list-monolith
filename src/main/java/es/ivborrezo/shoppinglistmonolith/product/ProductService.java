package es.ivborrezo.shoppinglistmonolith.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.category.CategoryRepository;
import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;
import es.ivborrezo.shoppinglistmonolith.user.User;
import es.ivborrezo.shoppinglistmonolith.user.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

	private ProductRepository productRepository;

	private UserRepository userRepository;

	private CategoryRepository categoryRepository;

	ProductService(ProductRepository productRepository, UserRepository userRepository,
			CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
	}

	public Page<Product> getAllProductsOfUser(Long id, int pageNumber, int pageSize) {

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		return productRepository.findByUserId(id, pageable);
	}

	/**
	 * Adds a product for a user identified by their user ID.
	 *
	 * @param userId  The unique identifier of the user.
	 * @param product The product to be added for the user.
	 * @return The product added and persisted in the database.
	 * @throws ResourceNotFoundException if the user or any of the categories
	 *                                   associated with the product are not found.
	 */
	public Product addProductByUserId(Long userId, Product product) {
		// Retrieve the user by userId from the database or throw
		// ResourceNotFoundException if not found
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId,
						"addProductByUserId", "userId", userId.toString()));

		// Check if each category associated with the product exists in the database or
		// throw ResourceNotFoundException
		product.getCategoryList().stream().forEach(category -> {
			if (!categoryRepository.existsById(category.getCategoryId())) {
				throw new ResourceNotFoundException("Category not found with id: " + category.getCategoryId(),
						"addProductByUserId", "categoryId", category.getCategoryId().toString());
			}
		});

		// Add the product to the user
		user.addProduct(product);

		// Save the product in the database and return the persisted product
		return productRepository.save(product);
	}

	@Transactional
	public void deleteByProductIdAndUserId(Long productId, Long userId) {

		if (!productRepository.existsByProductIdAndUserId(productId, userId))
			throw new ResourceNotFoundException("User not found with id: " + userId, "addProductByUserId", "userId",
					userId.toString());

		productRepository.deleteById(productId);
	}
}
