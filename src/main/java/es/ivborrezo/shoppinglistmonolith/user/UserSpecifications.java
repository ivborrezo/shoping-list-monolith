package es.ivborrezo.shoppinglistmonolith.user;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
	public static Specification<User> likeUserName(String userName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("userName")),
				"%" + userName.toLowerCase() + "%");

	}

	public static Specification<User> likeEmail(String email) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
				"%" + email.toLowerCase() + "%");
	}

	public static Specification<User> likeFirstName(String firstName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
				"%" + firstName.toLowerCase() + "%");
	}

	public static Specification<User> likeLastName(String lastName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
				"%" + lastName.toLowerCase() + "%");
	}

	public static Specification<User> byDateOfBirthGreaterThan(LocalDate dateOfBirth) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("dateOfBirth"), dateOfBirth);
	}

	public static Specification<User> byDateOfBirthLessThan(LocalDate dateOfBirth) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("dateOfBirth"), dateOfBirth);
	}

	public static Specification<User> likePhoneNumber(String phoneNumber) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")),
				"%" + phoneNumber.toLowerCase() + "%");
	}

}
