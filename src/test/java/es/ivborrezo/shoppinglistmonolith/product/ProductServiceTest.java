package es.ivborrezo.shoppinglistmonolith.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	private Product macarrones;
	private Product tomatico;
	private Product alcachofas;

	@BeforeEach
	public void setupTestData() {
		// Arrange

		macarrones = Product.builder().name("Macarrones").description("Macarrones ricos").price(3.45).brand("Gallo")
				.groceryChain("Eroski").build();
		tomatico = Product.builder().name("Tomatico").description("Tomatico rico rico").price(4.75).brand("Heinz")
				.groceryChain("Eroski").build();
		alcachofas = Product.builder().name("Alcachofas").description("Mehhh").price(5.00).brand("Marca blanca")
				.groceryChain("TodoTodo").build();
	}

	@Test
	void ProductService_GetAllProductsOfUser_ReturnPageOfProducts() {
		// Arrange
		Page<Product> pageProducts = new PageImpl<Product>(Arrays.asList(macarrones, tomatico));

		when(productRepository.findByUserId(anyLong(), any(Pageable.class))).thenReturn(pageProducts);
		// Act
		Page<Product> pageReturnedProducts = productService.getAllProductsOfUser(1L, 0, 10);

		// Assert
		assertEquals(2, pageReturnedProducts.getTotalElements());
		assertThat(pageReturnedProducts.getContent()).contains(macarrones);
		assertThat(pageReturnedProducts.getContent()).contains(tomatico);
		assertThat(pageReturnedProducts.getContent()).doesNotContain(alcachofas);
	}
}
