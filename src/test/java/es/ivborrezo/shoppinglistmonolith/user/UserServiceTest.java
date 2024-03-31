package es.ivborrezo.shoppinglistmonolith.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private User elyoya;
	private User myrwn;

	@BeforeEach
	public void setupTestData() {
		// Arrange
		LocalDate dateEloya = LocalDate.of(2000, 3, 18);
		LocalDate dateMyrwn = LocalDate.of(2001, 3, 18);

		elyoya = User.builder().userName("Elyoya").email("elyoya@gmail.com").firstName("El").lastName("Yoya")
				.password("asd").dateOfBirth(dateEloya).build();

		myrwn = User.builder().userName("Myrwn").email("myrwn@gmail.com").firstName("Myr").lastName("Wn")
				.password("asd").dateOfBirth(dateMyrwn).build();
	}

	@Test
	void UserService_GetUserById_ReturnUser() {
		// Arrange
		Long id = elyoya.getUserId();
		when(userRepository.findById(id)).thenReturn(Optional.ofNullable(elyoya));

		// Act
		User obtainedUser = userService.getUserById(id);

		// Assert
		verify(userRepository, times(1)).findById(id);
		assertThat(obtainedUser).isNotNull();

	}

	@Test
	void UserService_GetUserById_ThrowsExceptionIfNotFound() {
		// Arrange
		Long id = 1L;
		when(userRepository.findById(id)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(id));
		verify(userRepository, times(1)).findById(id);
	}
}
