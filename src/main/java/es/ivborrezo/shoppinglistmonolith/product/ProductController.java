package es.ivborrezo.shoppinglistmonolith.product;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.ivborrezo.shoppinglistmonolith.product.dto.ProductInputDTO;
import es.ivborrezo.shoppinglistmonolith.product.dto.ProductInputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.product.dto.ProductOutputDTO;
import es.ivborrezo.shoppinglistmonolith.product.dto.ProductOutputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.utils.Constants;
import es.ivborrezo.shoppinglistmonolith.utils.CriteriaOrderConverter;
import es.ivborrezo.shoppinglistmonolith.validationgroups.BasicValidation;

@RestController
@RequestMapping("/api/v1/")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	private ProductService productService;

	private ProductInputDTOMapper productInputDTOMapper;

	private ProductOutputDTOMapper productOutputDTOMapper;

	public ProductController(ProductService productService, ProductInputDTOMapper productInputDTOMapper,
			ProductOutputDTOMapper productOutputDTOMapper) {
		this.productService = productService;
		this.productInputDTOMapper = productInputDTOMapper;
		this.productOutputDTOMapper = productOutputDTOMapper;
	}

	/**
	 * Retrieves a page of ProductOutputDTO objects based on specified filters and
	 * sorting criteria.
	 *
	 * @param nameFilter         The name filter to apply on product names. Default
	 *                           is an empty string.
	 * @param descriptionFilter  The description filter to apply on product
	 *                           descriptions. Default is an empty string.
	 * @param priceGreater       The minimum price filter to apply on products. Can
	 *                           be null.
	 * @param priceLess          The maximum price filter to apply on products. Can
	 *                           be null.
	 * @param brandFilter        The brand filter to apply on product brands.
	 *                           Default is an empty string.
	 * @param groceryChainFilter The grocery chain filter to apply on product
	 *                           grocery chains. Default is an empty string.
	 * @param categoryIdsFilter  The set of category IDs filter to apply on product
	 *                           categories. Default is an empty set.
	 * @param page               The page number of the result set to retrieve.
	 *                           Default is 0.
	 * @param size               The size of each page in the result set. Default is
	 *                           10.
	 * @param sort               The list of sorting criteria to apply on the result
	 *                           set. Default is an empty list.
	 * @return A ResponseEntity containing a page of ProductOutputDTO objects based
	 *         on the specified filters and sorting criteria.
	 */
	@RequestMapping("products")
	public ResponseEntity<Page<ProductOutputDTO>> getProductBySpecification(
			@RequestParam(defaultValue = Constants.EMPTY) String nameFilter,
			@RequestParam(defaultValue = Constants.EMPTY) String descriptionFilter,
			@RequestParam(required = false) Double priceGreater, @RequestParam(required = false) Double priceLess,
			@RequestParam(defaultValue = Constants.EMPTY) String brandFilter,
			@RequestParam(defaultValue = Constants.EMPTY) String groceryChainFilter,
			@RequestParam(defaultValue = Constants.EMPTY) Set<Long> categoryIdsFilter,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "") List<String> sort) {

		// Convert sorting criteria to Spring Data JPA's Sort.Order objects
		List<Sort.Order> orderList = CriteriaOrderConverter.createAndMapSortOrder(sort,
				ProductOutputDTOMapper.getFieldMappings());

		// Retrieve a page of ProductOutputDTO objects based on specified filters and
		// sorting criteria
		Page<ProductOutputDTO> pageProductDTO = productService.getProductsBySpecification(nameFilter, descriptionFilter,
				priceGreater, priceLess, brandFilter, groceryChainFilter, categoryIdsFilter, page, size, orderList)
				.map(this.productOutputDTOMapper);

		logger.info(
				"Retrieved {} products with filters: nameFilter={}, descriptionFilter={}, priceGreater={}, priceLess={}, brandFilter={}, groceryChainFilter={}, categoryIdsFilter={}, page={}, size={}, sort={}",
				pageProductDTO.getNumberOfElements(), nameFilter, descriptionFilter, priceGreater, priceLess,
				brandFilter, groceryChainFilter, categoryIdsFilter, page, size, sort);

		return new ResponseEntity<>(pageProductDTO, HttpStatus.OK);
	}

	@RequestMapping("users/{id}/products")
	public ResponseEntity<Page<ProductOutputDTO>> getProductsByUserId(@PathVariable Long id,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<ProductOutputDTO> pageProductDTO = productService.getAllProductsOfUser(id, page, size)
				.map(productOutputDTOMapper);

		return new ResponseEntity<>(pageProductDTO, HttpStatus.OK);
	}

	/**
	 * Endpoint for adding a product for a user specified by their user ID.
	 *
	 * @param userId          The unique identifier of the user to whom the product
	 *                        will be added.
	 * @param productInputDTO The DTO (Data Transfer Object) containing information
	 *                        about the product to be added.
	 * @return A ResponseEntity containing the DTO representation of the added
	 *         product with HTTP status CREATED (201).
	 */
	@RequestMapping(method = RequestMethod.POST, value = "users/{userId}/products")
	public ResponseEntity<ProductOutputDTO> addProductByUserId(@PathVariable Long userId,
			@Validated(BasicValidation.class) @RequestBody ProductInputDTO productInputDTO) {

		logger.info("Received request to add product for user with ID: {}", userId);
		logger.debug("ProductInputDTO received: {}", productInputDTO);

		// Map the input DTO to a Product entity
		Product product = productInputDTOMapper.apply(productInputDTO);

		// Add the product for the specified user and map it to an output DTO
		ProductOutputDTO productOutputDTO = productOutputDTOMapper
				.apply(productService.addProductByUserId(userId, product));

		logger.info("Product added successfully for user with ID: {}", userId);
		logger.debug("ProductOutputDTO returned: {}", productOutputDTO);

		// Return a ResponseEntity containing the output DTO with HTTP CREATED (201)
		return new ResponseEntity<>(productOutputDTO, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "users/{userId}/products/{productId}")
	public ResponseEntity<Void> deleteByProductIdAndUserId(@PathVariable Long userId, @PathVariable Long productId) {

		productService.deleteByProductIdAndUserId(productId, userId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
