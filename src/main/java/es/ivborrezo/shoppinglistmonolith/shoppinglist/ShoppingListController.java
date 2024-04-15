package es.ivborrezo.shoppinglistmonolith.shoppinglist;

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

import es.ivborrezo.shoppinglistmonolith.shoppinglist.dto.ShoppingListInputDTO;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.dto.ShoppingListInputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.dto.ShoppingListOutputDTO;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.dto.ShoppingListOutputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.validationgroups.BasicValidation;

@RestController
@RequestMapping("/api/v1/")
public class ShoppingListController {

	private ShoppingListService shoppingListService;

	private ShoppingListInputDTOMapper shoppingListInputDTOMapper;

	private ShoppingListOutputDTOMapper shoppingListOutputDTOMapper;

	public ShoppingListController(ShoppingListService shoppingListService,
			ShoppingListInputDTOMapper shoppingListInputDTOMapper,
			ShoppingListOutputDTOMapper shoppingListOutputDTOMapper) {
		this.shoppingListService = shoppingListService;
		this.shoppingListInputDTOMapper = shoppingListInputDTOMapper;
		this.shoppingListOutputDTOMapper = shoppingListOutputDTOMapper;
	}

	@RequestMapping("users/{id}/shopping-lists")
	public ResponseEntity<Page<ShoppingListOutputDTO>> getShoppingListsByUserId(@PathVariable Long id,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<ShoppingListOutputDTO> pageShoppingListDTO = shoppingListService.getAllShoppingListsOfUser(id, page, size)
				.map(shoppingListOutputDTOMapper);

		return new ResponseEntity<>(pageShoppingListDTO, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "users/{userId}/shopping-lists")
	public ResponseEntity<ShoppingListOutputDTO> addShoppingListByUserId(@PathVariable Long userId,
			@Validated(BasicValidation.class) @RequestBody ShoppingListInputDTO shoppingListInputDTO) {

		ShoppingList shoppingList = shoppingListInputDTOMapper.apply(shoppingListInputDTO);

		ShoppingListOutputDTO shoppingListOutputDTO = shoppingListOutputDTOMapper
				.apply(shoppingListService.addShoppingListByUserId(userId, shoppingList));

		return new ResponseEntity<>(shoppingListOutputDTO, HttpStatus.CREATED);
	}
}
