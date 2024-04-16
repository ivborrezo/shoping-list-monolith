package es.ivborrezo.shoppinglistmonolith.product;

import org.springframework.data.domain.Page;
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
import es.ivborrezo.shoppinglistmonolith.validationgroups.BasicValidation;

@RestController
@RequestMapping("/api/v1/")
public class ProductController {

	private ProductService productService;

	private ProductInputDTOMapper productInputDTOMapper;

	private ProductOutputDTOMapper productOutputDTOMapper;

	public ProductController(ProductService productService, ProductInputDTOMapper productInputDTOMapper,
			ProductOutputDTOMapper productOutputDTOMapper) {
		this.productService = productService;
		this.productInputDTOMapper = productInputDTOMapper;
		this.productOutputDTOMapper = productOutputDTOMapper;
	}

	@RequestMapping("users/{id}/products")
	public ResponseEntity<Page<ProductOutputDTO>> getProductsByUserId(@PathVariable Long id,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<ProductOutputDTO> pageProductDTO = productService.getAllProductsOfUser(id, page, size)
				.map(productOutputDTOMapper);

		return new ResponseEntity<>(pageProductDTO, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "users/{userId}/products")
	public ResponseEntity<ProductOutputDTO> addProductByUserId(@PathVariable Long userId,
			@Validated(BasicValidation.class) @RequestBody ProductInputDTO productInputDTO) {

		Product product = productInputDTOMapper.apply(productInputDTO);

		ProductOutputDTO productOutputDTO = productOutputDTOMapper
				.apply(productService.addProductByUserId(userId, product));

		return new ResponseEntity<>(productOutputDTO, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "users/{userId}/products/{productId}")
	public ResponseEntity<Void> deleteByProductIdAndUserId(@PathVariable Long userId, @PathVariable Long productId) {

		productService.deleteByProductIdAndUserId(productId, userId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
