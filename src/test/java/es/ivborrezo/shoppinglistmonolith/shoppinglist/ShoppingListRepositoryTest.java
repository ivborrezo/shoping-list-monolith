package es.ivborrezo.shoppinglistmonolith.shoppinglist;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import es.ivborrezo.shoppinglistmonolith.user.User;

@DataJpaTest
public class ShoppingListRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ShoppingListRepository shoppingListRepository;

	ShoppingList listYoya;
	ShoppingList listYoya2;
	ShoppingList listMyr;
	private User elyoya;
	private User myrwn;
	private Pageable pageable;

	@BeforeEach
	public void setupTestData() {
		// Arrange: Create and persist a couple of shoppimg lists using EntityManager

		elyoya = User.builder().userId(1L).userName("Elyoya").email("elyoya@gmail.com").firstName("El").lastName("Yoya")
				.password("asd").dateOfBirth(null).phoneNumber("928374650").build();

		myrwn = User.builder().userId(2L).userName("Myrwn").email("myrwn@gmail.com").firstName("Myr").lastName("Wn")
				.password("asd").dateOfBirth(null).build();
		
		listYoya = ShoppingList.builder().name("Yoya list").creationDate(LocalDateTime.now()).owner(elyoya).build();
		listYoya2 = ShoppingList.builder().name("Yoya list2").creationDate(LocalDateTime.now()).owner(elyoya).build();
		listMyr = ShoppingList.builder().name("Myr list").creationDate(LocalDateTime.now()).owner(myrwn).build();

		pageable = PageRequest.of(0, 10);

		entityManager.persistAndFlush(listYoya);
		entityManager.persistAndFlush(listYoya2);
		entityManager.persistAndFlush(listMyr);
	}

	@Test
	public void ShoppingListRepository_FindByUserId_ReturnShoppingListsOfAUser() {

		Page<ShoppingList> pageShoppingLists = shoppingListRepository.findByUserId(1L, pageable);

		assertThat(pageShoppingLists.getContent()).contains(listYoya);
		assertThat(pageShoppingLists.getContent()).contains(listYoya2);
		assertThat(pageShoppingLists.getContent()).doesNotContain(listMyr);
	}
}
