package es.ivborrezo.shoppinglistmonolith.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class CriteriaOrderConverterTest {

	@Test
	public void CriteriaOrderConverter_CreateSortOrder_ReturnListSortOrder() {
		// Arrange
		String field1 = "field1";
		String field2 = "field2";
		String field3 = "field3";
		List<String> sortList = Arrays.asList(field1, Constants.MINUS + field2, field3);

		// Act
		List<Sort.Order> result = CriteriaOrderConverter.createSortOrder(sortList);

		// Assert
		assertEquals(3, result.size());
		assertTrue(result.get(0).getDirection().equals(Sort.Direction.ASC));
		assertTrue(result.get(0).getProperty().equals(field1));
		assertTrue(result.get(1).getDirection().equals(Sort.Direction.DESC));
		assertTrue(result.get(1).getProperty().equals(field2));
		assertTrue(result.get(2).getDirection().equals(Sort.Direction.ASC));
		assertTrue(result.get(2).getProperty().equals(field3));
	}

	@Test
	public void CriteriaOrderConverter_createSortOrder_ReturnListSortOrder() {
		// Arrange
		String field1 = "";
		String field2 = "field2";
		String field3 = "field3";
		List<String> sortList = Arrays.asList(field1, Constants.MINUS + field2, field3);

		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> {
			CriteriaOrderConverter.createSortOrder(sortList);
		});
	}
}
