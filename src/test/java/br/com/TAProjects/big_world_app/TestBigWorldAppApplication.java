package br.com.TAProjects.big_world_app;

import org.springframework.boot.SpringApplication;

public class TestBigWorldAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(BigWorldAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
