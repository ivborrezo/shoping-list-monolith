package es.ivborrezo.shoppinglistmonolith.shoppinglist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long>, JpaSpecificationExecutor<ShoppingList> {

	@Query("SELECT sl FROM ShoppingList sl WHERE sl.owner.userId = :userId")
	Page<ShoppingList> findByUserId(Long userId, Pageable pageable);
	
}
