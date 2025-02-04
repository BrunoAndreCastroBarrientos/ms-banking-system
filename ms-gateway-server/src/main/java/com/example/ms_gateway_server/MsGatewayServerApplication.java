package com.example.ms_gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsGatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsGatewayServerApplication.class, args);
	}

}
