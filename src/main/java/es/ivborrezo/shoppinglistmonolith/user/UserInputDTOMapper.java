package es.ivborrezo.shoppinglistmonolith.user;

import java.util.function.Function;

import org.springframework.stereotype.Service;

@Service
public class UserInputDTOMapper implements Function<UserInputDTO, User> {

	@Override
	public User apply(UserInputDTO userInputDTO) {

		return User.builder().userId(null)
				.userName(userInputDTO.getName())
				.email(userInputDTO.getEmail())
				.password(userInputDTO.getPassword())
				.firstName(userInputDTO.getFirstName())
				.lastName(userInputDTO.getLastName())
				.dateOfBirth(userInputDTO.getDateOfBirth())
				.phoneNumber(userInputDTO.getPhoneNumber())
				.build();
	}
}
