package es.ivborrezo.shoppinglistmonolith.user.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import es.ivborrezo.shoppinglistmonolith.product.ProductController;
import es.ivborrezo.shoppinglistmonolith.user.User;
import es.ivborrezo.shoppinglistmonolith.user.UserController;
import es.ivborrezo.shoppinglistmonolith.utils.Constants;
import lombok.Getter;

@Component
public class UserOutputDTOModelAssembler extends RepresentationModelAssemblerSupport<User, UserOutputDTOModel> {

	public UserOutputDTOModelAssembler() {
		super(UserController.class, UserOutputDTOModel.class);
	}

	@Override
	public UserOutputDTOModel toModel(User user) {
		UserOutputDTOModel userOutputDTOModel = new UserOutputDTOModel(user.getUserId(), user.getUserName(),
				user.getEmail(), user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getPhoneNumber());

		Link selfLink = linkTo(methodOn(UserController.class).getUserById(user.getUserId())).withSelfRel();
		userOutputDTOModel.add(selfLink);

		Link productLink = linkTo(methodOn(ProductController.class).getProductsByUserId(user.getUserId(),
				Constants.DEFAULT_PAGE_NUMBER, Constants.DEFAULT_PAGE_SIZE)).withRel("products");
		userOutputDTOModel.add(productLink);

		Link allLink = linkTo(methodOn(UserController.class).getUsersBySpecification(null, null, null, null, null, null,
				null, Constants.DEFAULT_PAGE_NUMBER, Constants.DEFAULT_PAGE_SIZE, null)).withRel("users").expand();
		userOutputDTOModel.add(allLink);

		return userOutputDTOModel;
	}

	/**
	 * Map to store mappings between User field names and UserOutputDTO field names
	 * that doesn't match. The key is the User field name. The value is the
	 * UserOutputDTO field name.
	 */
	@Getter
	private static final Map<String, String> fieldMappings = new HashMap<>();

	static {
		fieldMappings.put("id", "userId");
		fieldMappings.put("name", "userName");
	}

}
