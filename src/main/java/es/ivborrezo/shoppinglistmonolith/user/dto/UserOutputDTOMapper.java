package es.ivborrezo.shoppinglistmonolith.user.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import es.ivborrezo.shoppinglistmonolith.user.User;
import lombok.Getter;

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

	/**
	 * Map to store mappings between User field names and UserOutputDTO field names that doesn't match.
	 * The key is the User field name. The value is the UserOutputDTO field name.
	 */
	@Getter
	private static final Map<String, String> fieldMappings = new HashMap<>();

	static {
		fieldMappings.put("id", "userId");
		fieldMappings.put("name", "userName");
	}

}
