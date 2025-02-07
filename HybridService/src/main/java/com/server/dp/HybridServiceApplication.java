package com.server.dp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HybridServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HybridServiceApplication.class, args);
	}

}
