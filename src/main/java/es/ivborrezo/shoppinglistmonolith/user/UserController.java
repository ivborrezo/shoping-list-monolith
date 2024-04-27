package es.ivborrezo.shoppinglistmonolith.user;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
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
import es.ivborrezo.shoppinglistmonolith.user.dto.UserOutputDTOModel;
import es.ivborrezo.shoppinglistmonolith.utils.Constants;
import es.ivborrezo.shoppinglistmonolith.utils.CriteriaOrderConverter;
import es.ivborrezo.shoppinglistmonolith.validationgroups.BasicValidation;
import es.ivborrezo.shoppinglistmonolith.validationgroups.PatchValidation;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private UserService userService;

	private UserInputDTOMapper userInputDTOMapper;

	private UserOutputDTOMapper userOutputDTOMapper;

	private UserOutputDTOModelAssembler userOutputDTOModelAssembler;

	private PagedResourcesAssembler<User> pagedResourcesAssembler;

	public UserController(UserService userService, UserInputDTOMapper userInputDTOMapper,
			UserOutputDTOMapper userOutputDTOMapper, UserOutputDTOModelAssembler userOutputDTOModelAssembler,
			PagedResourcesAssembler<User> pagedResourcesAssembler) {
		this.userService = userService;
		this.userInputDTOMapper = userInputDTOMapper;
		this.userOutputDTOMapper = userOutputDTOMapper;
		this.userOutputDTOModelAssembler = userOutputDTOModelAssembler;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}

	@RequestMapping("users/all")
	public ResponseEntity<Page<UserOutputDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {

		Page<UserOutputDTO> pageUserDTO = userService.getAllUsers(page, size).map(this.userOutputDTOMapper);

		return new ResponseEntity<>(pageUserDTO, HttpStatus.OK);
	}

	/**
	 * Retrieves a paginated list of users based on specified filtering criteria and
	 * sorting parameters. The response follows HATEOAS.
	 *
	 * @param userFilter               Filter string for user names.
	 * @param emailFilter              Filter string for email addresses.
	 * @param firstNameFilter          Filter string for first names.
	 * @param lastNameFilter           Filter string for last names.
	 * @param dateOfBirthGreaterFilter Filter for users with date of birth greater
	 *                                 than or equal to this date.
	 * @param dateOfBirthLessFilter    Filter for users with date of birth less than
	 *                                 or equal to this date.
	 * @param phoneNumberFilter        Filter string for phone numbers.
	 * @param page                     Page number (zero-based).
	 * @param size                     Size of each page.
	 * @param sort                     List of sort criteria in the format
	 *                                 "property,asc|desc".
	 * @return ResponseEntity containing a PagedModel of UserOutputDTOModel.
	 */
	@RequestMapping("users")
	public ResponseEntity<PagedModel<UserOutputDTOModel>> getUsersBySpecification(
			@RequestParam(defaultValue = Constants.EMPTY) String userFilter,
			@RequestParam(defaultValue = Constants.EMPTY) String emailFilter,
			@RequestParam(defaultValue = Constants.EMPTY) String firstNameFilter,
			@RequestParam(defaultValue = Constants.EMPTY) String lastNameFilter,
			@RequestParam(required = false) LocalDate dateOfBirthGreaterFilter,
			@RequestParam(required = false) LocalDate dateOfBirthLessFilter,
			@RequestParam(defaultValue = Constants.EMPTY) String phoneNumberFilter,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "") List<String> sort) {

		// Convert sorting criteria to Spring Data JPA's Sort.Order objects
		List<Sort.Order> orderList = CriteriaOrderConverter.createAndMapSortOrder(sort,
				UserOutputDTOMapper.getFieldMappings());

		// Retrieve a page of User objects based on specified filters and sorting
		// criteria
		Page<User> pageUser = userService.getUsersBySpecification(userFilter, emailFilter, firstNameFilter,
				lastNameFilter, dateOfBirthGreaterFilter, dateOfBirthLessFilter, phoneNumberFilter, page, size,
				orderList);

		logger.info(
				"Retrieved {} users with filters: userFilter={}, emailFilter={}, firstNameFilter={}, lastNameFilter={}, dateOfBirthGreaterFilter={}, dateOfBirthLessFilter={}, phoneNumberFilter={}, page={}, size={}, sort={}",
				pageUser.getNumberOfElements(), userFilter, emailFilter, firstNameFilter, lastNameFilter,
				dateOfBirthGreaterFilter, dateOfBirthLessFilter, phoneNumberFilter, page, size, sort);

		// Convert User objects to UserOutputDTOModel and wrap them into a PagedModel in
		// order to follow HATEOAS
		PagedModel<UserOutputDTOModel> pageModelUserDTO = pagedResourcesAssembler.toModel(pageUser,
				userOutputDTOModelAssembler);

		return new ResponseEntity<>(pageModelUserDTO, HttpStatus.OK);
	}

	/**
	 * Retrieves a user by their unique identifier.
	 *
	 * @param id The unique identifier of the user.
	 * @return ResponseEntity containing the UserOutputDTOModel representing the retrieved user.
	 */
	@RequestMapping("users/{id}")
	public ResponseEntity<UserOutputDTOModel> getUserById(@PathVariable Long id) {

		logger.info("Retrieving user with ID: {}", id);
		
		UserOutputDTOModel userDTOModel = userOutputDTOModelAssembler.toModel(userService.getUserById(id));
		logger.info("Retrieved user with ID: {}", id);

		return new ResponseEntity<>(userDTOModel, HttpStatus.OK);
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
	public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {

		userService.deleteUserById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
