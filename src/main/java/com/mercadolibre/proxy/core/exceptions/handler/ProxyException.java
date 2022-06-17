package com.mercadolibre.proxy.core.exceptions.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProxyException {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> formatoInvalidoException(Exception exception)
	{
		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("error", exception.getMessage());
		return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
	}
	
}
