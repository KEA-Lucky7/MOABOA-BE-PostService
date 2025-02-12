package com.example.lucky7postservice;

import com.example.lucky7postservice.utils.openfeign.HeaderConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
@ImportAutoConfiguration({HeaderConfig.class})
public class Lucky7PostServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(Lucky7PostServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
