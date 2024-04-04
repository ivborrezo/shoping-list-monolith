package es.ivborrezo.shoppinglistmonolith.user;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;

@Service
public class UserService {

	UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Page<User> getAllUsers(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return userRepository.findAll(pageable);
	}

	public Page<User> getUsersBySpecification(String userName, String email, String firstName, String lastName,
			LocalDate dateOfBirthGreater, LocalDate dateOfBirthLess, String phoneNumber, int pageNumber, int pageSize) {

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		Specification<User> spec = Specification.where(null);

		if (userName != null && !userName.isEmpty())
			spec = spec.and(UserSpecifications.likeUserName(userName));
		
		if (email != null && !email.isEmpty())
			spec = spec.and(UserSpecifications.likeEmail(email));
		
		if (firstName != null && !firstName.isEmpty())
			spec = spec.and(UserSpecifications.likeFirstName(firstName));
		
		if (lastName != null && !lastName.isEmpty())
			spec = spec.and(UserSpecifications.likeLastName(lastName));
		
		if (dateOfBirthGreater != null)
			spec = spec.and(UserSpecifications.byDateOfBirthGreaterThan(dateOfBirthGreater));
		
		if (dateOfBirthLess != null)
			spec = spec.and(UserSpecifications.byDateOfBirthLessThan(dateOfBirthLess));
		
		if (phoneNumber != null && !phoneNumber.isEmpty())
			spec = spec.and(UserSpecifications.likePhoneNumber(phoneNumber));

		return userRepository.findAll(spec, pageable);
	}

	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id, "getUserById", "id",
						id.toString()));
	}

	public void deleteUserById(Long id) {
		userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id,
				"deleteUserById", "id", id.toString()));

		userRepository.deleteById(id);
	}
}
