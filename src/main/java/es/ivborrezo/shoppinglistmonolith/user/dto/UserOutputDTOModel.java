package es.ivborrezo.shoppinglistmonolith.user.dto;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOutputDTOModel extends RepresentationModel<UserOutputDTOModel> {
	
	Long id;
	String name;
	String email;
	String firstName;
	String lastName;
	LocalDate dateOfBirth;
	String phoneNumber;
	
}
