package es.ivborrezo.shoppinglistmonolith.user;

import java.time.LocalDate;

public record UserOutputDTO(Long id, String name, String email, String firstName, String lastName,
		LocalDate dateOfBirth, String phoneNumber) {

}
