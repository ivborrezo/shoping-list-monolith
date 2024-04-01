package es.ivborrezo.shoppinglistmonolith.user;

import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;

@Service
public class UserService {

	UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id, "getUserById", "id", id.toString()));
	}
	
	public void deleteUserById(Long id) {
		userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id, "deleteUserById", "id", id.toString()));
		
		userRepository.deleteById(id);
	}
}
