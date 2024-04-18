package es.ivborrezo.shoppinglistmonolith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShoppingListMonolithApplication {

	private static final Logger logger = LoggerFactory.getLogger(ShoppingListMonolithApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ShoppingListMonolithApplication.class, args);
		logger.info(
				"\n--------------------------------\nSpring Boot application started.\n--------------------------------");
	}

}
