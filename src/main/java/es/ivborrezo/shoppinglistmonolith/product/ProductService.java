package es.ivborrezo.shoppinglistmonolith.product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.category.CategoryRepository;
import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;
import es.ivborrezo.shoppinglistmonolith.user.User;
import es.ivborrezo.shoppinglistmonolith.user.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	private ProductRepository productRepository;

	private UserRepository userRepository;

	private CategoryRepository categoryRepository;

	ProductService(ProductRepository productRepository, UserRepository userRepository,
			CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
	}

	/**
	 * Retrieves a page of products based on the provided criteria.
	 *
	 * @param productName  The name of the product to filter by. Can be null or
	 *                     empty.
	 * @param description  The description of the product to filter by. Can be null
	 *                     or empty.
	 * @param priceGreater The minimum price of the product to filter by. Can be
	 *                     null.
	 * @param priceLess    The maximum price of the product to filter by. Can be
	 *                     null.
	 * @param brand        The brand of the product to filter by. Can be null or
	 *                     empty.
	 * @param groceryChain The grocery chain of the product to filter by. Can be
	 *                     null or empty.
	 * @param categoryIds  The set of category IDs to filter by. Can be null or
	 *                     empty.
	 * @param pageNumber   The page number of the result set to retrieve.
	 * @param pageSize     The size of each page in the result set.
	 * @param orderList    The list of sorting orders for the result set.
	 * @return A page of products based on the specified criteria.
	 */
	public Page<Product> getProductsBySpecification(String productName, String description, Double priceGreater,
			Double priceLess, String brand, String groceryChain, Set<Long> categoryIds, int pageNumber, int pageSize,
			List<Sort.Order> orderList) {

		// Create pageable object for pagination and sorting
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderList));

		// Initialize the specification with a null condition
		Specification<Product> spec = Specification.where(null);

		// Add filters to the specification if the filter strings are not null or empty
		if (productName != null && !productName.isEmpty())
			spec = spec.and(ProductSpecifications.likeProductName(productName));

		if (description != null && !description.isEmpty())
			spec = spec.and(ProductSpecifications.likeDescription(description));

		if (priceGreater != null)
			spec = spec.and(ProductSpecifications.byPriceGreaterThan(priceGreater));

		if (priceLess != null)
			spec = spec.and(ProductSpecifications.byPriceLessThan(priceLess));

		if (brand != null && !brand.isEmpty())
			spec = spec.and(ProductSpecifications.likeBrand(brand));

		if (groceryChain != null && !groceryChain.isEmpty())
			spec = spec.and(ProductSpecifications.likeGroceryChain(groceryChain));

//		if (categoryIds != null && categoryIds.size() > 0)
//			spec = spec.and(ProductSpecifications.byCategoryIds(categoryIds));
		
//		spec = spec.and(ProductSpecifications.likeCategoryName("f"));
//		spec = spec.and(ProductSpecifications.likeCategoryId(1L));

		// Retrieve products from the repository based on the specification and pageable
		Page<Product> pageProducts = productRepository.findAll(spec, pageable);

		logger.trace(
				"Retrieved {} products with filters: productName={}, description={}, priceGreater={}, priceLess={}, brand={}, groceryChain={}, categoryIds={}, pageNumber={}, pageSize={}, orderBy={}",
				pageProducts.getNumberOfElements(), productName, description, priceGreater, priceLess, brand,
				groceryChain, categoryIds, pageNumber, pageSize, orderList);

		return pageProducts;
	}

	/**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The product with the specified ID.
     * @throws ResourceNotFoundException if no product is found with the given ID.
     */
	public Product getProductById(Long id) {

		logger.trace("Attempting to retrieve product with ID: {}", id);

		Optional<Product> optionalProduct = productRepository.findById(id);
		if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			logger.info("Product found with ID {}", id);
			return product;
		} else {
			String errorMessage = String.format("Product not found with ID: %d", id);
			logger.error(errorMessage);
			throw new ResourceNotFoundException(errorMessage, "getProductById", "id", id.toString());
		}
	}

	/**
	 * Retrieves a page of products belonging to a specific user.
	 *
	 * @param id         The ID of the user whose products are to be retrieved.
	 * @param pageNumber The page number of the result set to retrieve.
	 * @param pageSize   The size of each page in the result set.
	 * @return A page of products belonging to the specified user.
	 */
	public Page<Product> getAllProductsOfUser(Long id, int pageNumber, int pageSize) {

		// Create pageable object for pagination
		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		// Retrieve products from the repository based on user ID and pageable
		Page<Product> pageProducts = productRepository.findByUserId(id, pageable);

		// Log the retrieval of products
		logger.trace("Retrieved {} products for user with ID {} (Page Number: {}, Page Size: {})",
				pageProducts.getNumberOfElements(), id, pageNumber, pageSize);

		return pageProducts;
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
		if (product.getCategoryList() != null) {
			product.getCategoryList().stream().forEach(category -> {
				if (!categoryRepository.existsById(category.getCategoryId())) {
					throw new ResourceNotFoundException("Category not found with id: " + category.getCategoryId(),
							"addProductByUserId", "categoryId", category.getCategoryId().toString());
				}
			});
		}

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
