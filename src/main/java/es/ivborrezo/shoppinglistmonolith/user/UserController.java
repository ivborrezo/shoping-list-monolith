package es.ivborrezo.shoppinglistmonolith.user;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping("/all")
	public ResponseEntity<Page<UserOutputDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {

		Page<UserOutputDTO> PageUserDTO = userService.getAllUsers(page, size).map(this.userOutputDTOMapper);

		return new ResponseEntity<>(PageUserDTO, HttpStatus.OK);
	}

	@RequestMapping("")
	public ResponseEntity<Page<UserOutputDTO>> getUsersBySpecification(
			@RequestParam(defaultValue = "") String userFilter, @RequestParam(defaultValue = "") String emailFilter,
			@RequestParam(defaultValue = "") String firstNameFilter,
			@RequestParam(defaultValue = "") String lastNameFilter,
			@RequestParam(required = false) LocalDate dateOfBirthGreaterFilter,
			@RequestParam(required = false) LocalDate dateOfBirthLessFilter,
			@RequestParam(defaultValue = "") String phoneNumberFilter, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {

		Page<UserOutputDTO> PageUserDTO = userService
				.getUsersBySpecification(userFilter, emailFilter, firstNameFilter, lastNameFilter,
						dateOfBirthGreaterFilter, dateOfBirthLessFilter, phoneNumberFilter, page, size)
				.map(this.userOutputDTOMapper);

		return new ResponseEntity<>(PageUserDTO, HttpStatus.OK);
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
