package es.ivborrezo.shoppinglistmonolith.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.ivborrezo.shoppinglistmonolith.user.dto.UserInputDTO;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserInputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserOutputDTO;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserOutputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.utils.Constants;
import es.ivborrezo.shoppinglistmonolith.validationgroups.BasicValidation;
import es.ivborrezo.shoppinglistmonolith.validationgroups.PatchValidation;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

	private UserService userService;

	private UserInputDTOMapper userInputDTOMapper;

	private UserOutputDTOMapper userOutputDTOMapper;

	public UserController(UserService userService, UserInputDTOMapper userInputDTOMapper,
			UserOutputDTOMapper userOutputDTOMapper) {
		this.userService = userService;
		this.userInputDTOMapper = userInputDTOMapper;
		this.userOutputDTOMapper = userOutputDTOMapper;
	}

	@RequestMapping("users/all")
	public ResponseEntity<Page<UserOutputDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {

		Page<UserOutputDTO> PageUserDTO = userService.getAllUsers(page, size).map(this.userOutputDTOMapper);

		return new ResponseEntity<>(PageUserDTO, HttpStatus.OK);
	}

	@RequestMapping("users")
	public ResponseEntity<Page<UserOutputDTO>> getUsersBySpecification(
			@RequestParam(defaultValue = Constants.EMPTY) String userFilter,
			@RequestParam(defaultValue = Constants.EMPTY) String emailFilter,
			@RequestParam(defaultValue = Constants.EMPTY) String firstNameFilter,
			@RequestParam(defaultValue = Constants.EMPTY) String lastNameFilter,
			@RequestParam(required = false) LocalDate dateOfBirthGreaterFilter,
			@RequestParam(required = false) LocalDate dateOfBirthLessFilter,
			@RequestParam(defaultValue = Constants.EMPTY) String phoneNumberFilter,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "") List<String> sort) {

		Page<UserOutputDTO> pageUserDTO = userService
				.getUsersBySpecification(userFilter, emailFilter, firstNameFilter, lastNameFilter,
						dateOfBirthGreaterFilter, dateOfBirthLessFilter, phoneNumberFilter, page, size, sort)
				.map(this.userOutputDTOMapper);

		return new ResponseEntity<>(pageUserDTO, HttpStatus.OK);
	}

	@RequestMapping("users/{id}")
	public ResponseEntity<UserOutputDTO> getUserById(@PathVariable Long id) {

		UserOutputDTO userDTO = userOutputDTOMapper.apply(userService.getUserById(id));

		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "users")
	public ResponseEntity<UserOutputDTO> addUser(
			@Validated(BasicValidation.class) @RequestBody UserInputDTO userInputDTO) {

		User user = userInputDTOMapper.apply(userInputDTO);

		UserOutputDTO userOutputDTO = userOutputDTOMapper.apply(userService.addUser(user));

		return new ResponseEntity<>(userOutputDTO, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "users/{id}")
	public ResponseEntity<UserOutputDTO> updateUserPartially(@PathVariable Long id,
			@Validated(PatchValidation.class) @RequestBody UserInputDTO userInputDTO) {

		User user = userInputDTOMapper.apply(userInputDTO);

		UserOutputDTO userOutputDTO = userOutputDTOMapper.apply(userService.updateUserPartially(id, user));

		return new ResponseEntity<>(userOutputDTO, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "users/{id}")
	public ResponseEntity<Void> deketeUserById(@PathVariable Long id) {

		userService.deleteUserById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
