package es.ivborrezo.shoppinglistmonolith.shoppinglist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;
import es.ivborrezo.shoppinglistmonolith.user.User;
import es.ivborrezo.shoppinglistmonolith.user.UserRepository;

@Service
public class ShoppingListService {

	private ShoppingListRepository shoppingListRepository;

	private UserRepository userRepository;

	ShoppingListService(ShoppingListRepository shoppingListRepository, UserRepository userRepository) {
		this.shoppingListRepository = shoppingListRepository;
		this.userRepository = userRepository;
	}

	public Page<ShoppingList> getAllShoppingListsOfUser(Long id, int pageNumber, int pageSize) {

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		return shoppingListRepository.findByUserId(id, pageable);
	}

	public ShoppingList addShoppingListByUserId(Long userId, ShoppingList shoppingList) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId,
						"addShoppingListByUserId", "userId", userId.toString()));

		user.addOwnedShoppingList(shoppingList);

		return shoppingListRepository.save(shoppingList);
	}
}
