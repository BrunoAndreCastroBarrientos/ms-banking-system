package com.nttdata.bootcamp.ms.banking.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MsBankcreditApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsBankcreditApplication.class, args);
	}

}
