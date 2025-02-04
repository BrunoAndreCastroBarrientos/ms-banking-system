package com.nttdata.bootcamp.ms.banking.bootcoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MsMobilewalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsMobilewalletApplication.class, args);
	}

}
