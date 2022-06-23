package com.mercadolibre.proxy.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
	


	@Bean("clienteRest")
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder)
	{

		SimpleClientHttpRequestFactory clientHttpRequestFactory
				= new SimpleClientHttpRequestFactory();
		//Connect timeout
		clientHttpRequestFactory.setConnectTimeout(300);

		//Read timeout
		clientHttpRequestFactory.setReadTimeout(300);


		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(2);

		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

		//template.setRetryPolicy(retryPolicy);
		//return restTemplateBuilder.setConnectTimeout(Duration.ofMillis(300)).setReadTimeout(Duration.ofMillis(300)).build();
		return restTemplate;
	}




}
