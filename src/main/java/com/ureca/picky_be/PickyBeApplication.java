package com.ureca.picky_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PickyBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PickyBeApplication.class, args);
	}

}
