package es.ivborrezo.shoppinglistmonolith.user.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInputDTO {

	@NotBlank(message = "name can not be empty")
	private String name;
	
	@NotBlank(message = "email can not be empty")
	private String email;

	@NotBlank(message = "password can not be empty")
	private String password;

	@NotBlank(message = "firstName can not be empty")
	private String firstName;

	@NotBlank(message = "lastName can not be empty")
	private String lastName;
	
	@NotNull(message = "dateOfBirth can not be empty")
	@Past(message = "dateOfBirth must be in the empty")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	@Pattern(regexp = "^$|^(\\+[0-9]{1,3})?[0-9]{8,14}$", message = "Invalid mobile phone format")
	private String phoneNumber;
	
}
