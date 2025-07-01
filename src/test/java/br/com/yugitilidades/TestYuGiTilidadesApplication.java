package br.com.yugitilidades;

import org.springframework.boot.SpringApplication;

public class TestYuGiTilidadesApplication {

	public static void main(String[] args) {
		SpringApplication.from(YuGiTilidadesApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
