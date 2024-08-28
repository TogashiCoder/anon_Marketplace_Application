package com.marketplace;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Marketplace REST API Documentation",
				description = "This documentation provides details about the REST API endpoints for  a marketplace platform. ",
				version = "v1",
				contact = @Contact(
						name = "Boucetta Taoufik",
						email = "taoufik.b.pro@gmail.com",
						url = "https://www.linkedin.com/in/boucettataoufik/"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.apache.org/licenses/LICENSE-2.0"
				)

		)
)
public class MarketplaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketplaceApplication.class, args);
	}

}
