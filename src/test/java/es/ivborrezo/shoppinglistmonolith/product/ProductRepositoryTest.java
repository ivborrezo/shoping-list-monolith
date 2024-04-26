package es.ivborrezo.shoppinglistmonolith.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import es.ivborrezo.shoppinglistmonolith.category.Category;
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

	private Category rico;
	private Category verde;

	private Pageable pageable;

	@BeforeEach
	public void setupTestData() {
		// Arrange: Create and persist a couple of products using EntityManager

		elyoya = User.builder().userId(1L).userName("Elyoya").email("elyoya@gmail.com").firstName("El").lastName("Yoya")
				.password("asd").dateOfBirth(null).phoneNumber("928374650").build();

		myrwn = User.builder().userId(2L).userName("Myrwn").email("myrwn@gmail.com").firstName("Myr").lastName("Wn")
				.password("asd").dateOfBirth(null).build();

		rico = Category.builder().categoryId(1L).categoryName("rico").build();

		verde = Category.builder().categoryId(2L).categoryName("verde").build();

		macarrones = Product.builder().productName("Macarrones").description("Macarrones ricos").price(3.45)
				.brand("Gallo").groceryChain("Eroski").user(elyoya).categoryList(Arrays.asList(rico)).build();
		tomatico = Product.builder().productName("Tomatico").description("Tomatico rico rico").price(4.75)
				.brand("Heinz").groceryChain("Eroski").user(elyoya).categoryList(Arrays.asList(rico, verde)).build();
		alcachofas = Product.builder().productName("Alcachofas").description("Mehhh").price(5.00).brand("Marca blanca")
				.groceryChain("TodoTodo").user(myrwn).categoryList(Arrays.asList(verde)).build();

		pageable = PageRequest.of(0, 100);

		entityManager.persistAndFlush(macarrones);
		entityManager.persistAndFlush(tomatico);
		entityManager.persistAndFlush(alcachofas);
	}

	@Test
	public void ProductRepository_FindAll_FilterProductName() {

		Page<Product> pageProduct = productRepository.findAll(ProductSpecifications.likeProductName("macarrone"),
				pageable);

		assertThat(pageProduct.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageProduct.getContent()).contains(macarrones);
		assertThat(pageProduct.getContent()).doesNotContain(tomatico);
	}

	@Test
	public void ProductRepository_FindAll_FilterDescription() {

		Page<Product> pageProduct = productRepository.findAll(ProductSpecifications.likeDescription("tomatico rico"),
				pageable);

		assertThat(pageProduct.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageProduct.getContent()).contains(tomatico);
		assertThat(pageProduct.getContent()).doesNotContain(macarrones);
	}

	@Test
	public void ProductRepository_FindAll_FilterPriceGreaterThan() {

		Page<Product> pageProduct = productRepository.findAll(ProductSpecifications.byPriceGreaterThan(4.70), pageable);

		assertThat(pageProduct.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageProduct.getContent()).doesNotContain(macarrones);
	}

	@Test
	public void ProductRepository_FindAll_FilterPriceLessThan() {
		Page<Product> pageProduct = productRepository.findAll(ProductSpecifications.byPriceLessThan(4.70), pageable);

		assertThat(pageProduct.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageProduct.getContent()).doesNotContain(tomatico);
	}

	@Test
	public void ProductRepository_FindAll_FilterBrand() {

		Page<Product> pageProduct = productRepository.findAll(ProductSpecifications.likeBrand("Hein"), pageable);

		assertThat(pageProduct.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageProduct.getContent()).contains(tomatico);
		assertThat(pageProduct.getContent()).doesNotContain(macarrones);
	}

	@Test
	public void ProductRepository_FindAll_FilterGroceryChain() {

		Page<Product> pageProduct = productRepository.findAll(ProductSpecifications.likeGroceryChain("Erosk"),
				pageable);

		assertThat(pageProduct.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageProduct.getContent()).contains(tomatico);
		assertThat(pageProduct.getContent()).contains(macarrones);
		assertThat(pageProduct.getContent()).doesNotContain(alcachofas);
	}

	@Test
	public void ProductRepository_FindAll_FilterCategoryId() {

		Page<Product> pageProduct = productRepository.findAll(ProductSpecifications.byCategoryId(rico.getCategoryId())
				.and(ProductSpecifications.byCategoryId(verde.getCategoryId())), pageable);

		assertThat(pageProduct.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageProduct.getContent()).contains(tomatico);
		assertThat(pageProduct.getContent()).doesNotContain(macarrones);
		assertThat(pageProduct.getContent()).doesNotContain(alcachofas);
	}

	@Test
	public void ProductRepository_FindAll_FilterCategoryName() {

		pageable = PageRequest.of(0, 10);

		Page<Product> pageProduct = productRepository.findAll(ProductSpecifications.likeCategoryName(""), pageable);

		assertThat(pageProduct.getTotalElements()).isGreaterThanOrEqualTo(1);
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

		Boolean exist = productRepository.existsByProductIdAndUserId(1L, 1L);

		assertThat(exist).isEqualTo(true);
	}

	@Test
	public void ProductRepository_ExistsByIdAndUserId_ReturnFalse() {

		Boolean exist = productRepository.existsByProductIdAndUserId(100L, 1L);

		assertThat(exist).isEqualTo(false);
	}

	@Test
	public void ProductRepository_Delete_ProductDeleted() {
		// Act: Delete the product from the repository
		productRepository.delete(macarrones);

		// Assert: Verify that the product is deleted
		assertThat(entityManager.find(Product.class, macarrones.getProductId())).isNull();
	}
}
