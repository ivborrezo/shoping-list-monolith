package es.ivborrezo.shoppinglistmonolith.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;
import es.ivborrezo.shoppinglistmonolith.user.User;
import es.ivborrezo.shoppinglistmonolith.user.UserRepository;

@Service
public class ProductService {

	private ProductRepository productRepository;

	private UserRepository userRepository;

	ProductService(ProductRepository productRepository, UserRepository userRepository) {
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}

	public Page<Product> getAllProductsOfUser(Long id, int pageNumber, int pageSize) {

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		return productRepository.findByUserId(id, pageable);
	}

	public Product addProductByUserId(Long userId, Product product) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId,
						"addProductByUserId", "userId", userId.toString()));

		user.addProduct(product);

		return productRepository.save(product);
	}
	
	public void deleteByProductIdAndUserId(Long productId, Long userId) {
		
		productRepository.existsByProductIdAndUserId(productId, userId);
		
		productRepository.deleteById(productId);
	}
}
