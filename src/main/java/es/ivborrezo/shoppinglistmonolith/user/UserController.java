package es.ivborrezo.shoppinglistmonolith.user;

import java.time.LocalDate;

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

import es.ivborrezo.shoppinglistmonolith.product.ProductService;
import es.ivborrezo.shoppinglistmonolith.product.dto.ProductOutputDTO;
import es.ivborrezo.shoppinglistmonolith.product.dto.ProductOutputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserInputDTO;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserInputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserOutputDTO;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserOutputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.validationgroups.BasicValidation;
import es.ivborrezo.shoppinglistmonolith.validationgroups.PatchValidation;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	UserService userService;

	UserInputDTOMapper userInputDTOMapper;

	UserOutputDTOMapper userOutputDTOMapper;

	ProductService productService;

	ProductOutputDTOMapper productOutputDTOMapper;

	public UserController(UserService userService, UserInputDTOMapper userInputDTOMapper,
			UserOutputDTOMapper userOutputDTOMapper, ProductService productService,
			ProductOutputDTOMapper productOutputDTOMapper) {
		this.userService = userService;
		this.userInputDTOMapper = userInputDTOMapper;
		this.userOutputDTOMapper = userOutputDTOMapper;
		this.productService = productService;
		this.productOutputDTOMapper = productOutputDTOMapper;
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
			@RequestParam(defaultValue = "10") int size) {

		Page<UserOutputDTO> pageUserDTO = userService
				.getUsersBySpecification(userFilter, emailFilter, firstNameFilter, lastNameFilter,
						dateOfBirthGreaterFilter, dateOfBirthLessFilter, phoneNumberFilter, page, size)
				.map(this.userOutputDTOMapper);

		return new ResponseEntity<>(pageUserDTO, HttpStatus.OK);
	}

	@RequestMapping("/{id}")
	public ResponseEntity<UserOutputDTO> getUserById(@PathVariable Long id) {

		UserOutputDTO userDTO = userOutputDTOMapper.apply(userService.getUserById(id));

		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@RequestMapping("/{id}/products")
	public ResponseEntity<Page<ProductOutputDTO>> getProductsByUserId(@PathVariable Long id,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<ProductOutputDTO> pageProductDTO = productService.getAllProductsOfUser(id, page, size)
				.map(productOutputDTOMapper);

		return new ResponseEntity<>(pageProductDTO, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "")
	public ResponseEntity<UserOutputDTO> addUser(
			@Validated(BasicValidation.class) @RequestBody UserInputDTO userInputDTO) {

		User user = userInputDTOMapper.apply(userInputDTO);

		UserOutputDTO userOutputDTO = userOutputDTOMapper.apply(userService.addUser(user));

		return new ResponseEntity<>(userOutputDTO, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	public ResponseEntity<UserOutputDTO> updateUserPartially(@PathVariable Long id,
			@Validated(PatchValidation.class) @RequestBody UserInputDTO userInputDTO) {

		User user = userInputDTOMapper.apply(userInputDTO);

		UserOutputDTO userOutputDTO = userOutputDTOMapper.apply(userService.updateUserPartially(id, user));

		return new ResponseEntity<>(userOutputDTO, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<Void> deketeUserById(@PathVariable Long id) {

		userService.deleteUserById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
