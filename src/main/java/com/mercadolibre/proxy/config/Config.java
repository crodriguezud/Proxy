package com.mercadolibre.proxy.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class Config {

	@Bean("clienteRest")
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer()
	{
		return factory -> factory.configureDefault(id -> {
			return new Resilience4JConfigBuilder(id)
					.circuitBreakerConfig(CircuitBreakerConfig.custom()
							.slidingWindowSize(10)//Por defecto = 100
							.failureRateThreshold(50)//Por defecto es el 50%
							.waitDurationInOpenState(Duration.ofSeconds(10L))//Tiempo de espera en el estado abierto, por defecto son 60 segundos
							.permittedNumberOfCallsInHalfOpenState(5)//Llamadas permitidas en estado semi abierto, por defecto = 10
							.slowCallRateThreshold(50) //Llamadas lentas
							.slowCallDurationThreshold(Duration.ofSeconds(2L))
							.build())
					.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3L)).build())
					.build();
		});
	}


}
