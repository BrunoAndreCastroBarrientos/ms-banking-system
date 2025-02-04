package com.nttdata.bootcamp.ms.banking.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MsTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTransactionApplication.class, args);
	}

}
