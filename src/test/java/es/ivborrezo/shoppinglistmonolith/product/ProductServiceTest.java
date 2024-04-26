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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import es.ivborrezo.shoppinglistmonolith.category.Category;
import es.ivborrezo.shoppinglistmonolith.category.CategoryRepository;
import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;
import es.ivborrezo.shoppinglistmonolith.user.User;
import es.ivborrezo.shoppinglistmonolith.user.UserRepository;
import es.ivborrezo.shoppinglistmonolith.user.dto.UserOutputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.utils.Constants;
import es.ivborrezo.shoppinglistmonolith.utils.CriteriaOrderConverter;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private ProductService productService;

	private Product macarrones;
	private Product tomatico;
	private Product alcachofas;
	
	private Category rico;
	private Category verde;

	@BeforeEach
	public void setupTestData() {
		// Arrange

		rico = Category.builder().categoryId(1L).categoryName("rico").build();
		verde = Category.builder().categoryId(2L).categoryName("verde").build();
		
		
		macarrones = Product.builder().productId(1L).productName("Macarrones").description("Macarrones ricos")
				.price(3.45).brand("Gallo").groceryChain("Eroski").categoryList(Arrays.asList(rico)).build();
		tomatico = Product.builder().productId(2L).productName("Tomatico").description("Tomatico rico rico").price(4.75)
				.brand("Heinz").groceryChain("Eroski").categoryList(Arrays.asList(rico, verde)).build();
		alcachofas = Product.builder().productId(3L).productName("Alcachofas").description("Mehhh").price(5.00)
				.brand("Marca blanca").groceryChain("TodoTodo").categoryList(Arrays.asList(verde)).build();
	}

	@Test
	void ProductService_GetProductsBySpecification_ReturnProducts() {
		// Arrange
		Page<Product> pageProducts = new PageImpl<Product>(Arrays.asList(macarrones, tomatico, alcachofas));

		List<Sort.Order> orderList = CriteriaOrderConverter.createAndMapSortOrder(Arrays.asList("name"),
				UserOutputDTOMapper.getFieldMappings());
		Pageable pageable = PageRequest.of(0, 10, Sort.by(orderList));

		when(productRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Product>>any(),
				org.mockito.ArgumentMatchers.eq(pageable))).thenReturn(pageProducts);

		// Act
		Set<Long> set = new HashSet<>(Arrays.asList(1L));
		Page<Product> returnedPageProduct = productService.getProductsBySpecification("a", "a", null, null, "a", "a",
				set, "a", 0, 10, orderList);

		// Assert

		assertEquals(3, returnedPageProduct.getTotalElements());
		assertThat(returnedPageProduct.getContent()).contains(macarrones);
		assertThat(returnedPageProduct.getContent()).contains(tomatico);
		assertThat(returnedPageProduct.getContent()).contains(alcachofas);
	}

	@Test
	void ProductService_GetProductsBySpecificationn_ReturnProductsFiltrados() {
		// Arrange
		Page<Product> pageProducts = new PageImpl<Product>(Arrays.asList(macarrones));

		List<Sort.Order> orderList = CriteriaOrderConverter.createAndMapSortOrder(Arrays.asList("name"),
				UserOutputDTOMapper.getFieldMappings());
		Pageable pageable = PageRequest.of(0, 10, Sort.by(orderList));

		when(productRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Product>>any(),
				org.mockito.ArgumentMatchers.eq(pageable))).thenReturn(pageProducts);
		// Act

		Page<Product> returnedPageProduct = productService.getProductsBySpecification(null, null, 100.0, 100.0, null,
				null, null, null, 0, 10, orderList);

		// Assert
		assertEquals(1, returnedPageProduct.getTotalElements());
		assertThat(returnedPageProduct.getContent()).contains(macarrones);
		assertThat(returnedPageProduct.getContent()).doesNotContain(tomatico);
		assertThat(returnedPageProduct.getContent()).doesNotContain(alcachofas);
	}

	@Test
	void ProductService_GetProductById_ReturnProduct() {
		// Arrange
		Long id = macarrones.getProductId();
		when(productRepository.findById(id)).thenReturn(Optional.ofNullable(macarrones));

		// Act
		Product obtainedProduct = productService.getProductById(id);

		// Assert
		verify(productRepository, times(1)).findById(id);
		assertThat(obtainedProduct).isNotNull();
		assertThat(obtainedProduct.getCategoryList()).contains(rico);
		assertThat(obtainedProduct.getCategoryList()).doesNotContain(verde);

	}

	@Test
	void ProductService_GetProductById_ThrowsExceptionIfNotFound() {
		// Arrange
		Long id = macarrones.getProductId();
		when(productRepository.findById(id)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(id));
		verify(productRepository, times(1)).findById(id);
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
		User elyoya = User.builder().userId(Constants.LONG_ONE).userName("Elyoya").email("elyoya@gmail.com")
				.firstName("El").lastName("Yoya").password("asd").dateOfBirth(LocalDate.of(2000, 3, 18))
				.phoneNumber("928374650").build();

		Long idUser = elyoya.getUserId();

		Category category1 = Category.builder().categoryId(Constants.LONG_ONE).build();
		Category category2 = Category.builder().categoryId(Constants.LONG_TWO).build();

		macarrones.setCategoryList(Arrays.asList(category1, category2));

		when(userRepository.findById(idUser)).thenReturn(Optional.ofNullable(elyoya));
		when(categoryRepository.existsById(any())).thenReturn(true);
		when(productRepository.save(any())).thenReturn(macarrones);

		// Act
		Product obtainedProduct = productService.addProductByUserId(idUser, macarrones);

		// Assert
		verify(userRepository, times(1)).findById(any());
		verify(categoryRepository, times(2)).existsById(any());
		verify(productRepository, times(1)).save(any());
		assertThat(obtainedProduct).isEqualTo(macarrones);
		assertThat(macarrones.getUser().getUserId()).isEqualTo(idUser);

	}

	@Test
	void ProductService_AddProductByUserIdWithoutCategory_ReturnProduct() {
		// Arrange
		User elyoya = User.builder().userId(1L).userName("Elyoya").email("elyoya@gmail.com").firstName("El")
				.lastName("Yoya").password("asd").dateOfBirth(LocalDate.of(2000, 3, 18)).phoneNumber("928374650")
				.build();

		Long idUser = elyoya.getUserId();

		when(userRepository.findById(idUser)).thenReturn(Optional.ofNullable(elyoya));
		when(categoryRepository.existsById(any())).thenReturn(true);
		when(productRepository.save(any())).thenReturn(macarrones);

		// Act
		Product obtainedProduct = productService.addProductByUserId(idUser, macarrones);

		// Assert
		verify(userRepository, times(1)).findById(any());
		verify(categoryRepository, times(1)).existsById(any());
		verify(productRepository, times(1)).save(any());
		assertThat(obtainedProduct).isEqualTo(macarrones);
		assertThat(macarrones.getUser().getUserId()).isEqualTo(idUser);

	}

	@Test
	void ProductService_AddProductByUserId_ThrowsExceptionIfUserNotFound() {
		// Arrange
		Long idUser = 1L;

		when(userRepository.findById(idUser)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(ResourceNotFoundException.class, () -> productService.addProductByUserId(idUser, macarrones));

		verify(userRepository, times(1)).findById(any());
		verify(productRepository, times(0)).save(any());
	}

	@Test
	void ProductService_AddProductByUserId_ThrowsExceptionIfCategoryNotFound() {
		// Arrange
		User elyoya = User.builder().userId(Constants.LONG_ONE).userName("Elyoya").email("elyoya@gmail.com")
				.firstName("El").lastName("Yoya").password("asd").dateOfBirth(LocalDate.of(2000, 3, 18))
				.phoneNumber("928374650").build();

		Long idUser = elyoya.getUserId();

		Category category1 = Category.builder().categoryId(Constants.LONG_ONE).build();
		Category category2 = Category.builder().categoryId(Constants.LONG_TWO).build();

		macarrones.setCategoryList(Arrays.asList(category1, category2));

		when(userRepository.findById(idUser)).thenReturn(Optional.ofNullable(elyoya));
		when(categoryRepository.existsById(any())).thenReturn(false);

		// Act and Assert
		assertThrows(ResourceNotFoundException.class, () -> productService.addProductByUserId(idUser, macarrones));

		verify(categoryRepository, times(1)).existsById(any());
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
