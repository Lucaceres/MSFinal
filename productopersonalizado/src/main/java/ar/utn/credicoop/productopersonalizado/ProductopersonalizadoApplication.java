package ar.utn.credicoop.productopersonalizado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProductopersonalizadoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductopersonalizadoApplication.class, args);
	}

}
