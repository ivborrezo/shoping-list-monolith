package es.ivborrezo.shoppinglistmonolith;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.ivborrezo.shoppinglistmonolith.user.UserController;

@SpringBootTest
class ShoppingListMonolithApplicationTests {

	@Autowired
	UserController controller;
	
	@Test
	void contextLoads() throws Exception {
	
		assertThat(controller).isNotNull();
	
	}

}
