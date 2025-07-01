package br.com.yugitilidades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class YuGiTilidadesApplication {

	public static void main(String[] args) {
		SpringApplication.run(YuGiTilidadesApplication.class, args);
	}

}
