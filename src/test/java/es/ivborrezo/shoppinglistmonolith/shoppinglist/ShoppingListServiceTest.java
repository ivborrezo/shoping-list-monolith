package es.ivborrezo.shoppinglistmonolith.shoppinglist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;
import es.ivborrezo.shoppinglistmonolith.user.User;
import es.ivborrezo.shoppinglistmonolith.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ShoppingListServiceTest {

	@Mock
	private ShoppingListRepository shoppingListRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ShoppingListService shoppingListService;

	ShoppingList listYoya;
	ShoppingList listYoya2;
	ShoppingList listMyr;

	@BeforeEach
	public void setupTestData() {
		// Arrange
		listYoya = ShoppingList.builder().name("Yoya list").creationDate(LocalDateTime.now()).build();
		listYoya2 = ShoppingList.builder().name("Yoya list2").creationDate(LocalDateTime.now()).build();
		listMyr = ShoppingList.builder().name("Myr list").creationDate(LocalDateTime.now()).build();
	}

	@Test
	void ShoppingListService_GetAllShoppingListsOfUser_ReturnPageOfShoppingLists() {
		// Arrange
		Page<ShoppingList> pageShoppingLists = new PageImpl<ShoppingList>(Arrays.asList(listYoya, listYoya2));

		when(shoppingListRepository.findByUserId(anyLong(), any(Pageable.class))).thenReturn(pageShoppingLists);
		// Act
		Page<ShoppingList> pageReturnedShoppingLists = shoppingListService.getAllShoppingListsOfUser(1L, 0, 10);

		// Assert
		assertEquals(2, pageReturnedShoppingLists.getTotalElements());
		assertThat(pageReturnedShoppingLists.getContent()).contains(listYoya);
		assertThat(pageReturnedShoppingLists.getContent()).contains(listYoya2);
		assertThat(pageReturnedShoppingLists.getContent()).doesNotContain(listMyr);
	}

	@Test
	void ShoppingListService_AddShoppingListByUserId_ReturnShoppingList() {
		// Arrange
		User elyoya = User.builder().userId(1L).userName("Elyoya").email("elyoya@gmail.com").firstName("El")
				.lastName("Yoya").password("asd").dateOfBirth(LocalDate.of(2000, 3, 18)).phoneNumber("928374650")
				.build();

		Long idUser = elyoya.getUserId();

		when(userRepository.findById(idUser)).thenReturn(Optional.ofNullable(elyoya));
		when(shoppingListRepository.save(any())).thenReturn(listYoya);

		// Act
		ShoppingList obtainedShoppingList = shoppingListService.addShoppingListByUserId(idUser, listYoya);

		// Assert
		verify(userRepository, times(1)).findById(any());
		verify(shoppingListRepository, times(1)).save(any());
		assertThat(obtainedShoppingList).isEqualTo(listYoya);
		assertThat(obtainedShoppingList.getOwner().getUserId()).isEqualTo(idUser);

	}

	@Test
	void ShoppingListService_AddShoppingListByUserId_ThrowsExceptionIfNotFound() {
		// Arrange
		Long idUser = 1L;

		when(userRepository.findById(idUser)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(ResourceNotFoundException.class,
				() -> shoppingListService.addShoppingListByUserId(idUser, listYoya));

		verify(userRepository, times(1)).findById(any());
		verify(shoppingListRepository, times(0)).save(any());

	}
}
