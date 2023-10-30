package med.voll.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Essa classe é criada para executar nossa aplicação
@SpringBootApplication
public class ApiApplication {

	//O método main é utilizado para rodar/inicializar o projeto
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
