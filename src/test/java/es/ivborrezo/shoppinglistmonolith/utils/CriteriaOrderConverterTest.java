package es.ivborrezo.shoppinglistmonolith.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void CriteriaOrderConverter_CreateSortOrder_ThrowExceptionWhenEmpty() {
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
	
	@Test
	public void CriteriaOrderConverter_CreateAndMapSortOrder_ReturnListSortOrderMapped() {
		// Arrange
		String field1 = "field1";
		String field2 = "field2";
		String field3 = "field3";
		String field4 = "field4";
		String field5 = "field5";
		List<String> sortList = Arrays.asList(field1, Constants.MINUS + field2, Constants.PLUS + field3, Constants.MINUS + field4, field5);
		
		String mappedField2 = "mappedField2";
		String mappedField3 = "mappedField3";
		String mappedField5 = "mappedField5";
		Map<String, String> dTOFieldToEntityFieldMap = new HashMap<String, String>();
		dTOFieldToEntityFieldMap.put(field2, mappedField2);
		dTOFieldToEntityFieldMap.put(field3, mappedField3);
		dTOFieldToEntityFieldMap.put(field5, mappedField5);

		// Act
		List<Sort.Order> result = CriteriaOrderConverter.createAndMapSortOrder(sortList, dTOFieldToEntityFieldMap);

		// Assert
		assertEquals(5, result.size());
		assertTrue(result.get(0).getDirection().equals(Sort.Direction.ASC));
		assertTrue(result.get(0).getProperty().equals(field1));
		assertTrue(result.get(1).getDirection().equals(Sort.Direction.DESC));
		assertTrue(result.get(1).getProperty().equals(mappedField2));
		assertTrue(result.get(2).getDirection().equals(Sort.Direction.ASC));
		assertTrue(result.get(2).getProperty().equals(mappedField3));
		assertTrue(result.get(3).getDirection().equals(Sort.Direction.DESC));
		assertTrue(result.get(3).getProperty().equals(field4));
		assertTrue(result.get(4).getDirection().equals(Sort.Direction.ASC));
		assertTrue(result.get(4).getProperty().equals(mappedField5));
	}

	@Test
	public void CriteriaOrderConverter_CreateAndMapSortOrder_ThrowExceptionWhenEmpty() {
		// Arrange
		String field1 = "";
		String field2 = "field2";
		String field3 = "field3";
		List<String> sortList = Arrays.asList(field1, Constants.MINUS + field2, field3);
		
		String mappedField2 = "mappedField2";
		String mappedField3 = "mappedField3";
		Map<String, String> dTOFieldToEntityFieldMap = new HashMap<String, String>();
		dTOFieldToEntityFieldMap.put(field2, mappedField2);
		dTOFieldToEntityFieldMap.put(field3, mappedField3);

		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> {
			CriteriaOrderConverter.createAndMapSortOrder(sortList, dTOFieldToEntityFieldMap);
		});
	}
}
