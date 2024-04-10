package es.ivborrezo.shoppinglistmonolith.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

	ProductRepository productRepository;

	ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public Page<Product> getAllProductsOfUser(Long id, int pageNumber, int pageSize){
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		return productRepository.findByUserId(id, pageable);
	}
}
