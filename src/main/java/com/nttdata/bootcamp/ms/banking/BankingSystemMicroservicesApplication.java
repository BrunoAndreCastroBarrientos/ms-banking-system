package com.nttdata.bootcamp.ms.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BankingSystemMicroservicesApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankingSystemMicroservicesApplication.class, args);
  }

}
