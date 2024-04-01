package es.ivborrezo.shoppinglistmonolith.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	UserService userService;

	UserOutputDTOMapper userOutputDTOMapper;

	public UserController(UserService userService, UserOutputDTOMapper userOutputDTOMapper) {
		this.userService = userService;
		this.userOutputDTOMapper = userOutputDTOMapper;
	}

	@RequestMapping("/{id}")
	public ResponseEntity<UserOutputDTO> getUserById(@PathVariable Long id) {

		UserOutputDTO userDTO = userOutputDTOMapper.apply(userService.getUserById(id));

		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<Void> deketeUserById(@PathVariable Long id) {
		
		userService.deleteUserById(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
