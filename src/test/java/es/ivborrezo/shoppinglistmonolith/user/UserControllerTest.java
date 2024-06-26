package es.ivborrezo.shoppinglistmonolith.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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

import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserInputDTO;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserInputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserOutputDTO;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserOutputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.utils.Constants;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@MockBean
	private UserInputDTOMapper userInputDTOMapper;

	@MockBean
	private UserOutputDTOMapper userOutputDTOMapper;

	private User elyoya;
	private User myrwn;

	@BeforeEach
	public void setupTestData() {
		// Arrange
		LocalDate dateEloya = LocalDate.of(2000, 3, 18);
		LocalDate dateMyrwn = LocalDate.of(2001, 3, 18);

		elyoya = User.builder().userId(1L).userName("Elyoya").email("elyoya@gmail.com").firstName("El").lastName("Yoya")
				.password("asd").dateOfBirth(dateEloya).phoneNumber("928374650").build();

		myrwn = User.builder().userId(2L).userName("Myrwn").email("myrwn@gmail.com").firstName("Myr").lastName("Wn")
				.password("asd").dateOfBirth(dateMyrwn).build();
	}

	@Test
	void UserController_GetUsersBySpecification_ReturnResponseEntity200WithPageOfUsers() throws Exception {
		// Arrange
		List<User> userList = Arrays.asList(elyoya, myrwn);
		// Create a PageRequest object to represent page settings (page number, page
		// size)
		PageRequest pageRequest = PageRequest.of(0, userList.size());

		// Create a Page<User> using PageImpl with the userList and pageRequest
		Page<User> userPage = new PageImpl<>(userList, pageRequest, userList.size());

		when(userService.getUsersBySpecification(any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt(), any()))
				.thenReturn(userPage);

		// Act
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/users").contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(2));

	}
	
	@Test
	void UserController_GetUsersBySpecification_ReturnResponseEntity500WhenSortValueEmpty() throws Exception {
		// Arrange
		String sortField1 = Constants.EMPTY;
		String sortField2 = "-email";

		// Act
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/users?{sortField1},{sortField2}", sortField1, sortField2).contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isInternalServerError());

	}

	@Test
	void UserController_GetUserById_ReturnResponseEntity200WithUser() throws Exception {
		// Arrange
		Long id = 1L;
		when(userService.getUserById(id)).thenReturn(elyoya);

		UserOutputDTO userOutputDTO = new UserOutputDTO(elyoya.getUserId(), elyoya.getUserName(), elyoya.getEmail(),
				elyoya.getFirstName(), elyoya.getLastName(), elyoya.getDateOfBirth(), elyoya.getPhoneNumber());

		when(userOutputDTOMapper.apply(elyoya)).thenReturn(userOutputDTO);

		// Act
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/users/{id}", id).contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Elyoya"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("elyoya@gmail.com"));

	}

	@Test
	void UserController_GetUserById_WhenNotFoudReturnResponseEntity404() throws Exception {
		// Arrange
		Long id = 1L;
		when(userService.getUserById(id)).thenThrow(ResourceNotFoundException.class);

		// Act
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/users/{id}", id).contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isNotFound());

	}

	@Test
	void UserController_GetUserById_WhenBadUrlResponseEntity500() throws Exception {
		// Arrange
		Long id = 1L;
		when(userService.getUserById(id)).thenThrow(NumberFormatException.class);

		// Act
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/users/{id}", id).contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isInternalServerError());

	}

	@Test
	void UserController_AddUser_ReturnResponseEntity201WithUser() throws Exception {
		// Arrange
		LocalDate dateEloya = LocalDate.of(2000, 3, 18);
		UserInputDTO elyoyaInputDTO = UserInputDTO.builder().name("Elyoya").email("elyoya@gmail.com").password("asd")
				.firstName("El").lastName("Yoya").password("asd").dateOfBirth(dateEloya).phoneNumber("928374650")
				.build();

		when(userService.addUser(ArgumentMatchers.any())).thenReturn(elyoya);

		UserOutputDTO userOutputDTO = new UserOutputDTO(elyoya.getUserId(), elyoya.getUserName(), elyoya.getEmail(),
				elyoya.getFirstName(), elyoya.getLastName(), elyoya.getDateOfBirth(), elyoya.getPhoneNumber());

		when(userOutputDTOMapper.apply(elyoya)).thenReturn(userOutputDTO);

		// Act
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(elyoyaInputDTO)));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Elyoya"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("elyoya@gmail.com"));
	}

	@Test
	void UserController_AddUser_WhenValidationFails_ThenReturns422() throws Exception {
		// Arrange
		LocalDate dateEloya = LocalDate.of(2000, 3, 18);
		String badName = Constants.EMPTY;
		String badEmail = "Godyoyagmail.com";
		UserInputDTO elyoyaInputDTO = UserInputDTO.builder().name(badName).email(badEmail).password(Constants.EMPTY).firstName("El")
				.lastName("Yoya").dateOfBirth(dateEloya).phoneNumber("928374650").build();

		// Act
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(elyoyaInputDTO)));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

		Mockito.verify(userService, Mockito.never()).addUser(Mockito.any());

	}

	@Test
	void UserController_UpdateUserPartially_ReturnResponseEntity201WithUser() throws Exception {
		// Arrange
		Long id = 1L;
		String newName = "JejeGod";
		String newEmail = "Godyoya@gmail.com";
		UserInputDTO elyoyaInputDTO = UserInputDTO.builder().name(newName).email(newEmail).build();

		User returnedElyoya = User.builder().userId(id).userName(newName).email(newEmail).password(elyoya.getPassword())
				.firstName(elyoya.getFirstName()).lastName(elyoya.getLastName()).dateOfBirth(elyoya.getDateOfBirth())
				.phoneNumber(elyoya.getPhoneNumber()).build();

		when(userService.updateUserPartially(any(), any())).thenReturn(returnedElyoya);

		UserOutputDTO userOutputDTO = new UserOutputDTO(returnedElyoya.getUserId(), returnedElyoya.getUserName(),
				returnedElyoya.getEmail(), returnedElyoya.getFirstName(), returnedElyoya.getLastName(),
				returnedElyoya.getDateOfBirth(), returnedElyoya.getPhoneNumber());

		when(userOutputDTOMapper.apply(any())).thenReturn(userOutputDTO);

		// Act
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(elyoyaInputDTO)));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newName))
				.andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(newEmail));
	}

	@Test
	void UserController_UpdateUserPartially_WhenValidationFails_ThenReturns422() throws Exception {
		// Arrange
		Long id = 1L;
		String newName = "JejeGod";
		String newEmail = "Godyoyagmail.com";
		UserInputDTO elyoyaInputDTO = UserInputDTO.builder().name(newName).email(newEmail).build();

		// Act
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(elyoyaInputDTO)));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

		Mockito.verify(userService, Mockito.never()).addUser(Mockito.any());

	}

	@Test
	void UserController_DeleteUserById_ReturnResponseEntity204Void() throws Exception {
		// Arrange
		Long id = 1L;
		Mockito.doNothing().when(userService).deleteUserById(id);

		// Act
		ResultActions response = mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/v1/users/{id}", id).contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isNoContent());
	}

	@Test
	void UserController_DeleteUserById_WhenNotFoudReturnResponseEntity404() throws Exception {
		// Arrange
		Long id = 1L;
		doThrow(ResourceNotFoundException.class).when(userService).deleteUserById(id);

		// Act
		ResultActions response = mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/v1/users/{id}", id).contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isNotFound());

	}
}
