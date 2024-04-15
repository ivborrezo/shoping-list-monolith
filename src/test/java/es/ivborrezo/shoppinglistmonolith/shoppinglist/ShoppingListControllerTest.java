package es.ivborrezo.shoppinglistmonolith.shoppinglist;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.ivborrezo.shoppinglistmonolith.shoppinglist.dto.ShoppingListInputDTO;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.dto.ShoppingListInputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.dto.ShoppingListOutputDTO;
import es.ivborrezo.shoppinglistmonolith.shoppinglist.dto.ShoppingListOutputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.utils.Constants;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ShoppingListController.class)
public class ShoppingListControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ShoppingListService shoppingListService;

	@MockBean
	private ShoppingListInputDTOMapper shoppingListInputDTOMapper;

	@MockBean
	private ShoppingListOutputDTOMapper shoppingListOutputDTOMapper;

	ShoppingList listYoya;
	ShoppingList listMyr;

	@BeforeEach
	public void setupTestData() {
		// Arrange
		listYoya = ShoppingList.builder().name("Yoya list").creationDate(LocalDateTime.now()).build();
		listMyr = ShoppingList.builder().name("Myr list").creationDate(LocalDateTime.now()).build();
	}

	@Test
	void UserController_GetShoppingListsByUserId_ReturnResponseEntity200WithPageOfShoppingLists() throws Exception {

		// Arrange

		List<ShoppingList> shoppingLists = Arrays.asList(listYoya, listMyr);
		// Create a PageRequest object to represent page settings (page number, page
		// size)
		PageRequest pageRequest = PageRequest.of(0, shoppingLists.size());

		// Create a Page<ShoppingList> using PageImpl with the shoppingLists and
		// pageRequest
		Page<ShoppingList> shoppingListPage = new PageImpl<>(shoppingLists, pageRequest, shoppingLists.size());

		when(shoppingListService.getAllShoppingListsOfUser(any(), anyInt(), anyInt())).thenReturn(shoppingListPage);

		// Act
		ResultActions response = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/users/1/shopping-lists").contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(2));
	}

	@Test
	void UserController_AddShoppingListByUserId_ReturnResponseEntity201WithShoppingList() throws Exception {
		// Arrange
		Long id = 1L;
		ShoppingListInputDTO shoppingListInputDTO = ShoppingListInputDTO.builder().name("Yoya list").build();

		when(shoppingListService.addShoppingListByUserId(anyLong(), any())).thenReturn(listYoya);

		ShoppingListOutputDTO listYoyaOutputDTO = new ShoppingListOutputDTO(listYoya.getShoppingListId(),
				listYoya.getName(), listYoya.getCreationDate());

		when(shoppingListOutputDTOMapper.apply(listYoya)).thenReturn(listYoyaOutputDTO);

		// Act
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/{id}/shopping-lists", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(shoppingListInputDTO)));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(shoppingListInputDTO.getName()));

	}

	@Test
	void UserController_AddShoppingListByUserId_WhenValidationFails_ThenReturns422() throws Exception {
		// Arrange
		Long id = 1L;
		String badName = Constants.EMPTY;
		ShoppingListInputDTO listYoyaInputDTO = ShoppingListInputDTO.builder().name(badName).build();

		// Act
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/{id}/shopping-lists", id)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(listYoyaInputDTO)));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

		Mockito.verify(shoppingListService, Mockito.never()).addShoppingListByUserId(id, listMyr);

	}
}
