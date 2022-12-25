package com.hvdbs.ssewebmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync //3.1
@SpringBootApplication
public class SsewebmvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsewebmvcApplication.class, args);
	}
}
