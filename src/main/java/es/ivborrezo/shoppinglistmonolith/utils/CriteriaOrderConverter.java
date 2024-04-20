package es.ivborrezo.shoppinglistmonolith.utils;

import java.util.List;
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
	
}
