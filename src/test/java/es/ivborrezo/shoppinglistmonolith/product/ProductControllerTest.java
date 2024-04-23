package es.ivborrezo.shoppinglistmonolith.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.ivborrezo.shoppinglistmonolith.exception.ResourceNotFoundException;
import es.ivborrezo.shoppinglistmonolith.product.dto.ProductInputDTO;
import es.ivborrezo.shoppinglistmonolith.product.dto.ProductInputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.product.dto.ProductOutputDTO;
import es.ivborrezo.shoppinglistmonolith.product.dto.ProductOutputDTOMapper;
import es.ivborrezo.shoppinglistmonolith.utils.Constants;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductService productService;

	@MockBean
	private ProductInputDTOMapper productInputDTOMapper;

	@MockBean
	private ProductOutputDTOMapper productOutputDTOMapper;

	private Product macarrones;
	private Product tomatico;
	private Product alcachofas;

	@BeforeEach
	public void setupTestData() {
		macarrones = Product.builder().productName("Macarrones").description("Macarrones ricos").price(3.45)
				.brand("Gallo").groceryChain("Eroski").build();
		tomatico = Product.builder().productName("Tomatico").description("Tomatico rico rico").price(4.75)
				.brand("Heinz").groceryChain("Eroski").build();
		alcachofas = Product.builder().productName("Alcachofas").description("Mehhh").price(5.00).brand("Marca blanca")
				.groceryChain("TodoTodo").build();
	}

	@Test
	void ProductController_GetProductsBySpecification_ReturnResponseEntity200WithPageOfProducts() throws Exception {
		// Arrange
		List<Product> productList = Arrays.asList(macarrones, tomatico, alcachofas);

		PageRequest pageRequest = PageRequest.of(0, productList.size());
		Page<Product> productPage = new PageImpl<Product>(productList, pageRequest, productList.size());

		when(productService.getProductsBySpecification(any(), any(), any(), any(), any(), any(), any(), anyInt(),
				anyInt(), any())).thenReturn(productPage);

		// Act
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/products").contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(3));
	}

	@Test
	void ProductController_GetProductsBySpecification__ReturnResponseEntity500WhenSortValueEmpty() throws Exception {
		// Arrange
		String sortField1 = Constants.EMPTY;
		String sortField2 = "-description";

		// Act
		ResultActions response = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/products?{sortField1},{sortField2}", sortField1, sortField2)
						.contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isInternalServerError());

	}

	@Test
	void ProductController_GetProductById_ReturnResponseEntity200WithProduct() throws Exception {

		// Arrange
		when(productService.getProductById(any())).thenReturn(macarrones);

		ProductOutputDTO macarronesOutputDto = new ProductOutputDTO(macarrones.getProductId(),
				macarrones.getProductName(), macarrones.getDescription(), macarrones.getPrice(), macarrones.getBrand(),
				macarrones.getGroceryChain());

		when(productOutputDTOMapper.apply(any())).thenReturn(macarronesOutputDto);

		// Act
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/products/1").contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(macarrones.getProductId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(macarrones.getProductName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(macarrones.getDescription()));
	}

	@Test
	void ProductController_GetProductById_WhenNotFoudReturnResponseEntity404() throws Exception {
		// Arrange
		Long id = 1L;
		when(productService.getProductById(any())).thenThrow(ResourceNotFoundException.class);

		// Act
		ResultActions response = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/products/{id}", id).contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isNotFound());

	}

	@Test
	void ProductController_GetProductById_WhenBadUrlResponseEntity500() throws Exception {
		// Arrange
		Long id = 1L;
		when(productService.getProductById(id)).thenThrow(NumberFormatException.class);

		// Act
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/users/{id}", id).contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isInternalServerError());

	}

	@Test
	void ProductController_GetProductsByUserId_ReturnResponseEntity200WithPageOfProducts() throws Exception {

		// Arrange
		Product macarrones = Product.builder().productName("Macarrones").description("Macarrones ricos").price(3.45)
				.brand("Gallo").groceryChain("Eroski").build();
		Product tomatico = Product.builder().productName("Tomatico").description("Tomatico rico rico").price(4.75)
				.brand("Heinz").groceryChain("Eroski").build();

		List<Product> productList = Arrays.asList(macarrones, tomatico);
		// Create a PageRequest object to represent page settings (page number, page
		// size)
		PageRequest pageRequest = PageRequest.of(0, productList.size());

		// Create a Page<Product> using PageImpl with the productList and pageRequest
		Page<Product> productPage = new PageImpl<>(productList, pageRequest, productList.size());

		when(productService.getAllProductsOfUser(any(), anyInt(), anyInt())).thenReturn(productPage);

		// Act
		ResultActions response = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/users/1/products").contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(2));
	}

	@Test
	void ProductController_AddProductByUserId_ReturnResponseEntity201WithProduct() throws Exception {
		// Arrange
		Long id = 1L;
		ProductInputDTO macarronesInputDTO = ProductInputDTO.builder().name("Macarrones")
				.description("Macarrones ricos").price(3.45).brand("Gallo").groceryChain("Eroski").build();

		Product macarrones = Product.builder().productName("Macarrones").description("Macarrones ricos").price(3.45)
				.brand("Gallo").groceryChain("Eroski").build();

		when(productService.addProductByUserId(anyLong(), any())).thenReturn(macarrones);

		ProductOutputDTO macarronesOutputDTO = new ProductOutputDTO(macarrones.getProductId(),
				macarrones.getProductName(), macarrones.getDescription(), macarrones.getPrice(), macarrones.getBrand(),
				macarrones.getGroceryChain());

		when(productOutputDTOMapper.apply(macarrones)).thenReturn(macarronesOutputDTO);

		// Act
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/{id}/products", id)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(macarronesInputDTO)));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(macarronesInputDTO.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(macarronesInputDTO.getDescription()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(macarronesInputDTO.getPrice()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.brand").value(macarronesInputDTO.getBrand()));

	}

	@Test
	void ProductController_AddProductByUserId_WhenValidationFails_ThenReturns422() throws Exception {
		// Arrange
		Long id = 1L;
		String badName = Constants.EMPTY;
		double badPrice = -1;
		ProductInputDTO macarronesInputDTO = ProductInputDTO.builder().name(badName).description("Macarrones ricos")
				.price(badPrice).brand("Gallo").groceryChain("Eroski").build();

		// Act
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/{id}/products", id)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(macarronesInputDTO)));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

		Mockito.verify(productService, Mockito.never()).addProductByUserId(anyLong(), any());

	}

	@Test
	void ProductController_DeleteByProductIdAndUserId_WhenValidationFails_ReturnResponseEntity204Void()
			throws Exception {
		// Arrange
		Mockito.doNothing().when(productService).deleteByProductIdAndUserId(anyLong(), anyLong());

		// Act
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.delete("/api/v1/users/{userId}/products/{productId}", 1L, 1L));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isNoContent());

	}
}
