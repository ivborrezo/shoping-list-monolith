/*
 * DTOMapping enum represents the mapping between DTO (Data Transfer Object) names and corresponding model class names.
 */
package es.ivborrezo.shoppinglistmonolith.enums;

public enum DTOMapping {
	/*
	 * USER_INPUT_DTO represents the mapping for user input DTO. dtoName:
	 * "UserInputDTO" modelClassName: "User"
	 */
	USER_INPUT_DTO("UserInputDTO", "User"),

	/*
	 * PRODUCT_INPUT_DTO represents the mapping for product input DTO. dtoName:
	 * "ProductInputDTO" modelClassName: "Product"
	 */
	PRODUCT_INPUT_DTO("ProductInputDTO", "Product"),

	/*
	 * SHOPPING_LIST_INPUT_DTO represents the mapping for shopping list input DTO.
	 * dtoName: "ShoppingListInputDTO" modelClassName: "ShoppingList"
	 */
	SHOPPING_LIST_INPUT_DTO("ShoppingListInputDTO", "ShoppingList");

	private final String dtoName; // The name of the DTO
	private final String modelClassName; // The name of the corresponding model class

	/*
	 * Constructor to initialize DTO name and model class name for each enum
	 * constant.
	 */
	DTOMapping(String dtoName, String modelClassName) {
		this.dtoName = dtoName;
		this.modelClassName = modelClassName;
	}

	/*
	 * Get the DTO name.
	 * 
	 * @return The name of the DTO.
	 */
	public String getDtoName() {
		return dtoName;
	}

	/*
	 * Get the model class name.
	 * 
	 * @return The name of the model class.
	 */
	public String getModelClassName() {
		return modelClassName;
	}

	/*
	 * Retrieve the model class name based on the given DTO name.
	 * 
	 * @param dtoName The name of the DTO.
	 * 
	 * @return The corresponding model class name, or null if no match is found.
	 */
	public static String getModelClassNameFromDtoName(String dtoName) {
		for (DTOMapping mapping : values()) {
			if (mapping.getDtoName().equalsIgnoreCase(dtoName)) {
				return mapping.getModelClassName();
			}
		}
		return null; // Return null if no match is found
	}
}