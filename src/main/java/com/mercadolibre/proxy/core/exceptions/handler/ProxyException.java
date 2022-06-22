package com.mercadolibre.proxy.core.exceptions.handler;

import com.mercadolibre.proxy.core.exceptions.InvalidUriException;
import com.mercadolibre.proxy.core.exceptions.UnlimitedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ProxyException {

	@ExceptionHandler(value = InvalidUriException.class)
	public ResponseEntity<Object> invalidUriException(InvalidUriException exception)
	{
		Map<String, Object> response = new HashMap<>();
		response.put("error", exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = UnlimitedException.class)
	public ResponseEntity<Object> unlimitedException(UnlimitedException exception)
	{
		Map<String, Object> response = new HashMap<>();
		response.put("error", exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
	}
	
}
