package es.ivborrezo.shoppinglistmonolith.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private UserRepository userRepository;

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

	@Test
	void ProductService_AddProductByUserId_ReturnProduct() {
		// Arrange
		User elyoya = User.builder().userId(1L).userName("Elyoya").email("elyoya@gmail.com").firstName("El")
				.lastName("Yoya").password("asd").dateOfBirth(LocalDate.of(2000, 3, 18)).phoneNumber("928374650")
				.build();

		Long idUser = elyoya.getUserId();

		when(userRepository.findById(idUser)).thenReturn(Optional.ofNullable(elyoya));
		when(productRepository.save(any())).thenReturn(macarrones);

		// Act
		Product obtainedProduct = productService.addProductByUserId(idUser, macarrones);

		// Assert
		verify(userRepository, times(1)).findById(any());
		verify(productRepository, times(1)).save(any());
		assertThat(obtainedProduct).isEqualTo(macarrones);
		assertThat(macarrones.getUser().getUserId()).isEqualTo(idUser);

	}

	@Test
	void ProductService_AddProductByUserId_ThrowsExceptionIfNotFound() {
		// Arrange
		Long idUser = 1L;

		when(userRepository.findById(idUser)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(ResourceNotFoundException.class, () -> productService.addProductByUserId(idUser, macarrones));

		verify(userRepository, times(1)).findById(any());
		verify(productRepository, times(0)).save(any());
	}
	
	@Test
	void ProductService_DeleteProductByUserId_DeleteCalled() {
		Long id = 1L;
		when(productRepository.existsByProductIdAndUserId(anyLong(), anyLong())).thenReturn(true);
		
		productService.deleteByProductIdAndUserId(id, id);
		
		verify(productRepository, times(1)).existsByProductIdAndUserId(anyLong(), anyLong());
		verify(productRepository, times(1)).deleteById(anyLong());
	}
	
	@Test
	void ProductService_DeleteByProductIdAndUserId_ThrowsExceptionIfNotFound() {
		Long id = 1L;
		when(productRepository.existsByProductIdAndUserId(anyLong(), anyLong())).thenReturn(false);
		
		assertThrows(ResourceNotFoundException.class, () -> productService.deleteByProductIdAndUserId(id, id));
		verify(productRepository, times(1)).existsByProductIdAndUserId(anyLong(), anyLong());
		verify(productRepository, times(0)).deleteById(anyLong());
	}
}
