package es.ivborrezo.shoppinglistmonolith.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

	UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}
}
