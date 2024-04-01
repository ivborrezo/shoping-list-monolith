package es.ivborrezo.shoppinglistmonolith.user;

import java.util.function.Function;

import org.springframework.stereotype.Service;

@Service
public class UserOutputDTOMapper implements Function<User, UserOutputDTO> {

	@Override
	public UserOutputDTO apply(User user) {
		
		return new UserOutputDTO(user.getUserId(), 
				user.getUserName(), 
				user.getEmail(), 
				user.getFirstName(), 
				user.getLastName(), 
				user.getDateOfBirth(), 
				user.getPhoneNumber());
	}

}
