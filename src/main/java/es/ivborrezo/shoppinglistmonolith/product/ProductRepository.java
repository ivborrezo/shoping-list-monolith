package es.ivborrezo.shoppinglistmonolith.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

	@Query("SELECT p FROM Product p WHERE p.user.userId = :userId")
	Page<Product> findByUserId(Long userId, Pageable pageable);

	@Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.id = :productId AND p.user.userId = :userId")
	boolean existsByProductIdAndUserId(Long productId, Long userId);

}
