package es.ivborrezo.shoppinglistmonolith.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;
import es.ivborrezo.shoppinglistmonolith.exception.UnprocessableEntityException;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserOutputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.utils.CriteriaOrderConverter;

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

		elyoya = User.builder().userId(1L).userName("Elyoya").email("elyoya@gmail.com").firstName("El").lastName("Yoya")
				.password("asd").dateOfBirth(dateEloya).phoneNumber("928374650").build();

		myrwn = User.builder().userId(2L).userName("Myrwn").email("myrwn@gmail.com").firstName("Myr").lastName("Wn")
				.password("asd").dateOfBirth(dateMyrwn).build();
	}

	@Test
	void UserService_GetUsersBySpecification_ReturnUsers() {
		// Arrange
		Page<User> pageUsers = new PageImpl<User>(Arrays.asList(elyoya, myrwn));

		List<Sort.Order> orderList = CriteriaOrderConverter.createAndMapSortOrder(Arrays.asList("name"),
				UserOutputDTOMapper.getFieldMappings());
		Pageable pageable = PageRequest.of(0, 10, Sort.by(orderList));

		when(userRepository.findAll(org.mockito.ArgumentMatchers.<Specification<User>>any(),
				org.mockito.ArgumentMatchers.eq(pageable))).thenReturn(pageUsers);

		// Act

		Page<User> pageUser = userService.getUsersBySpecification("y", "@gmail", null, null, LocalDate.of(1993, 3, 18),
				LocalDate.of(2003, 3, 18), null, 0, 10, orderList);

		// Assert

		assertEquals(2, pageUser.getTotalElements());
		assertThat(pageUser.getContent()).contains(elyoya);
		assertThat(pageUser.getContent()).contains(myrwn);
	}

	@Test
	void UserService_GetUsersBySpecification_ReturnUserFiltrado() {
		// Arrange
		Page<User> pageUsers = new PageImpl<User>(Arrays.asList(elyoya));

		List<Sort.Order> orderList = CriteriaOrderConverter.createAndMapSortOrder(Arrays.asList("name"),
				UserOutputDTOMapper.getFieldMappings());
		Pageable pageable = PageRequest.of(0, 10, Sort.by(orderList));

		when(userRepository.findAll(org.mockito.ArgumentMatchers.<Specification<User>>any(),
				org.mockito.ArgumentMatchers.eq(pageable))).thenReturn(pageUsers);
		// Act

		Page<User> pageUser = userService.getUsersBySpecification(null, null, "e", "y", null, null, "9", 0, 10, orderList);

		// Assert
		assertEquals(1, pageUser.getTotalElements());
		assertThat(pageUser.getContent()).contains(elyoya);
		assertThat(pageUser.getContent()).doesNotContain(myrwn);
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
		Long id = elyoya.getUserId();
		when(userRepository.findById(id)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(id));
		verify(userRepository, times(1)).findById(id);
	}

	@Test
	void UserService_AddUser_ReturnUser() {
		// Arrange
		LocalDate dateAlvaro = LocalDate.of(2000, 5, 6);

		User alvaro = User.builder().userName("Alvaro").email("alvaro@gmail.com").firstName("Ganchito")
				.lastName("Dalvarito").password("asd").dateOfBirth(dateAlvaro).phoneNumber("928374651").build();

		when(userRepository.save(Mockito.any(User.class))).thenReturn(alvaro);

		// Act
		User obtainedUser = userService.addUser(alvaro);

		// Assert
		verify(userRepository, times(1)).save(alvaro);
		assertThat(obtainedUser).isEqualTo(alvaro);

	}

	@Test
	void UserService_AddUser_ThrowsExceptionIfIdNotNull() {
		// Act and Assert
		assertThrows(UnprocessableEntityException.class, () -> userService.addUser(elyoya));

		assertAll(() -> {
			verify(userRepository, times(0)).save(elyoya);
		});
	}

	@Test
	void UserService_UpdateUserPartially_ReturnUser() {
		// Arrange
		Long id = elyoya.getUserId();
		String newName = "JejeGod";
		String newEmail = "Godyoya@gmail.com";
		User incompleteUser = User.builder().userName(newName).email(newEmail).build();

		User returnedElyoya = User.builder().userId(id).userName(newName).email(newEmail).password(elyoya.getPassword())
				.firstName(elyoya.getFirstName()).lastName(elyoya.getLastName()).dateOfBirth(elyoya.getDateOfBirth())
				.phoneNumber(elyoya.getPhoneNumber()).build();

		when(userRepository.findById(any())).thenReturn(Optional.ofNullable(elyoya));
		when(userRepository.save(any())).thenReturn(returnedElyoya);

		// Act
		userService.updateUserPartially(id, incompleteUser);

		// Assert
		assertAll(() -> {
			verify(userRepository, times(1)).findById(id);
			verify(userRepository, times(1)).save(returnedElyoya);
		});
		assertEquals(returnedElyoya, elyoya);
		assertThat(returnedElyoya.getUserName().equals(newName));
		assertThat(returnedElyoya.getEmail().equals(newEmail));
	}

	@Test
	void UserService_UpdateUserPartially_ThrowsExceptionIfNotFound() {
		// Arrange
		Long id = elyoya.getUserId();
		String newName = "JejeGod";
		User incompleteUser = User.builder().userName(newName).build();

		when(userRepository.findById(id)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(ResourceNotFoundException.class, () -> userService.updateUserPartially(id, incompleteUser));

		assertAll(() -> {
			verify(userRepository, times(1)).findById(id);
			verify(userRepository, times(0)).save(any());
		});
	}

	@Test
	void UserService_DeleteUserById_DeleteCalled() {
		// Arrange
		Long id = elyoya.getUserId();

		when(userRepository.findById(id)).thenReturn(Optional.ofNullable(elyoya));

		// Act
		userService.deleteUserById(id);

		// Assert
		assertAll(() -> {
			verify(userRepository, times(1)).findById(id);
			verify(userRepository, times(1)).deleteById(id);
		});
	}

	@Test
	void UserService_DeleteUserById_ThrowsExceptionIfNotFound() {
		// Arrange
		Long id = 1L;

		when(userRepository.findById(id)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(id));

		assertAll(() -> {
			verify(userRepository, times(1)).findById(id);
			verify(userRepository, times(0)).deleteById(id);
		});
	}

}
