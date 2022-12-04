package ar.utn.credicoop.productobase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProductobaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductobaseApplication.class, args);
	}

}
