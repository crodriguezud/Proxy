package com.mercadolibre.proxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
	
	@Bean("clienteRest")
	public RestTemplate registrarRestTemplate() 
	{
		return new RestTemplate();
	}
	
}
