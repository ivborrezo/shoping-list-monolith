package es.ivborrezo.shoppinglistmonolith.user;

import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	private User elyoya;

	@BeforeEach
	public void setupTestData() {
		// Arrange
		LocalDate dateEloya = LocalDate.of(2000, 3, 18);

		elyoya = User.builder().userName("Elyoya").email("elyoya@gmail.com").firstName("El").lastName("Yoya")
				.password("asd").dateOfBirth(dateEloya).build();
	}

	@Test
	void UserController_GetUserById_ReturnResponseEntity200WithUser() throws Exception {
		// Arrange
		Long id = 1L;
		elyoya.setUserId(id);
		when(userService.getUserById(id)).thenReturn(elyoya);

		// Act
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/users/{id}", id).contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("Elyoya"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("elyoya@gmail.com"));

	}
}
