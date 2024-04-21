package es.ivborrezo.shoppinglistmonolith.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

/**
 * Utility class to convert a list of sort criteria into a list of Spring Data Sort orders.
 */
public class CriteriaOrderConverter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CriteriaOrderConverter.class);
	
	/**
	 * Converts a list of sort criteria strings into a list of Spring Data Sort
	 * orders.
	 * 
	 * @param sortList A list of sort criteria strings.
	 * @return A list of Spring Data Sort orders.
	 * @throws IllegalArgumentException If the sort string is empty.
	 */
	public static List<Sort.Order> createSortOrder(List<String> sortList){
		
		LOGGER.info("Creating Sort orders from criteria: {}", sortList);
		
		return sortList.stream()
				.map(sort -> {
					if(sort.isEmpty()) {
						LOGGER.error("Sort string cannot be empty.");
						throw new IllegalArgumentException("Sort string cannot be empty");
					}
					if(sort.charAt(0) == Constants.CHAR_MINUS)
						return new Sort.Order(Sort.Direction.DESC, sort.substring(1)); 
					else
						return new Sort.Order(Sort.Direction.ASC, sort); 
				}).collect(Collectors.toList());
	}
	
	/**
	 * Creates a list of Sort.Order objects based on the provided sortList and a
	 * mapping of DTO fields to entity fields.
	 * 
	 * @param sortList                 List of strings representing sorting
	 *                                 instructions. Each string can start with '+'
	 *                                 for ascending order or '-' for descending
	 *                                 order.
	 * @param DTOFieldToEntityFieldMap A map that maps DTO field names to entity
	 *                                 field names. The key should be the User field
	 *                                 name. The value should be the UserOutputDTO
	 *                                 field name.
	 * @return A list of Sort.Order objects representing the sorting instructions.
	 * @throws IllegalArgumentException If the sort string is empty.
	 */
	public static List<Sort.Order> createAndMapSortOrder(List<String> sortList, Map<String, String> DTOFieldToEntityFieldMap){
		
		return sortList.stream()
				.map(sort -> {
					if(sort.isEmpty()) {
						LOGGER.error("Sort string cannot be empty.");
						throw new IllegalArgumentException("Sort string cannot be empty");
					}
					if(sort.charAt(0) == Constants.CHAR_MINUS) {
						String property = sort.substring(1);
						
						if(DTOFieldToEntityFieldMap.containsKey(property))
							property = DTOFieldToEntityFieldMap.get(property);
						
						return new Sort.Order(Sort.Direction.DESC, property); 
					}
					else if(sort.charAt(0) == Constants.CHAR_PLUS) {
						String property = sort.substring(1);
						
						if(DTOFieldToEntityFieldMap.containsKey(property))
							property = DTOFieldToEntityFieldMap.get(property);
						
						return new Sort.Order(Sort.Direction.ASC, property);
					}
					else {
						String property = sort;
						
						if(DTOFieldToEntityFieldMap.containsKey(property))
							property = DTOFieldToEntityFieldMap.get(property);
						
						return new Sort.Order(Sort.Direction.ASC, property);
					}
				}).collect(Collectors.toList());
	}
	
}
