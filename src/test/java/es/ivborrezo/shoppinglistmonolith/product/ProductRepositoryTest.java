package es.ivborrezo.shoppinglistmonolith.product;

import static org.assertj.core.api.Assertions.assertThat;

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
public class ProductRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ProductRepository productRepository;

	private Product macarrones;
	private Product tomatico;
	private Product alcachofas;
	private User elyoya;
	private User myrwn;
	private Pageable pageable;

	@BeforeEach
	public void setupTestData() {
		// Arrange: Create and persist a couple of products using EntityManager

		elyoya = User.builder().userId(1L).userName("Elyoya").email("elyoya@gmail.com").firstName("El").lastName("Yoya")
				.password("asd").dateOfBirth(null).phoneNumber("928374650").build();

		myrwn = User.builder().userId(2L).userName("Myrwn").email("myrwn@gmail.com").firstName("Myr").lastName("Wn")
				.password("asd").dateOfBirth(null).build();

		macarrones = Product.builder().name("Macarrones").description("Macarrones ricos").price(3.45).brand("Gallo")
				.groceryChain("Eroski").user(elyoya).build();
		tomatico = Product.builder().name("Tomatico").description("Tomatico rico rico").price(4.75).brand("Heinz")
				.groceryChain("Eroski").user(elyoya).build();
		alcachofas = Product.builder().name("Alcachofas").description("Mehhh").price(5.00).brand("Marca blanca")
				.groceryChain("TodoTodo").user(myrwn).build();

		pageable = PageRequest.of(0, 10);

		entityManager.persistAndFlush(macarrones);
		entityManager.persistAndFlush(tomatico);
		entityManager.persistAndFlush(alcachofas);
	}

	@Test
	public void ProductRepository_FindByUserId_ReturnProductsOfAUser() {

		Page<Product> pageProducts = productRepository.findByUserId(1L, pageable);

		assertThat(pageProducts.getContent()).contains(macarrones);
		assertThat(pageProducts.getContent()).contains(tomatico);
		assertThat(pageProducts.getContent()).doesNotContain(alcachofas);
	}
	
	@Test
	public void ProductRepository_ExistsByIdAndUserId_ReturnTrue() {

		Page<Product> pageProducts = productRepository.findByUserId(1L, pageable);
		
		Boolean exist = productRepository.existsByProductIdAndUserId(1L, 1L);

		assertThat(exist).isEqualTo(true);
	}
	
	@Test
	public void ProductRepository_ExistsByIdAndUserId_ReturnFalse() {
		
		Boolean exist = productRepository.existsByProductIdAndUserId(100L, 1L);

		assertThat(exist).isEqualTo(false);
	}
	
	@Test
	public void UserRepository_Delete_UserDeleted() {
		// Act: Delete the product from the repository
//		productRepository.deleteByProductIdAndUserId(macarrones.getProductId(), macarrones.getUser().getUserId());
		productRepository.delete(macarrones);
		
		// Assert: Verify that the product is deleted
		assertThat(entityManager.find(Product.class, macarrones.getProductId())).isNull();
	}
}
