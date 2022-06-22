package com.mercadolibre.proxy.core.controllers;

import com.mercadolibre.proxy.core.exceptions.InvalidUriException;
import com.mercadolibre.proxy.core.exceptions.UnlimitedException;
import com.mercadolibre.proxy.core.services.ProxyService;
import com.mercadolibre.proxy.utils.WildcardParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProxyController {

	private ProxyService proxyService;

	@Autowired
	public ProxyController (ProxyService proxyService) {
		this.proxyService = proxyService;
	}

	/**
	 *
	 * @param path
	 * @param reqPara
	 * @param request
	 * @return
	 * @exception
	 * @author Cristian Rodriguez - 21/06/2022
	 */
	@GetMapping("/**")
	public ResponseEntity<?> query(@WildcardParam String path, @RequestParam MultiValueMap<String, String> reqPara, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(proxyService.query(path, reqPara, request.getRemoteAddr(), "GET"), HttpStatus.OK);
		}catch(InvalidUriException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}catch(UnlimitedException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/**")
	public void update(@WildcardParam String path, @RequestParam MultiValueMap<String, String> reqPara, HttpServletRequest request) {
		proxyService.update(path, reqPara, request.getRemoteAddr(), "PUT");
	}
	
	@PostMapping(value="/**",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@WildcardParam String path, @RequestParam MultiValueMap<String, String> reqPara, @RequestBody Object request) {
		return new ResponseEntity<>(proxyService.create(path, reqPara, request, "POST"),HttpStatus.OK);
	}
	
	@DeleteMapping("/**")
	public void delete(@WildcardParam String path, @RequestParam MultiValueMap<String, String> reqPara, HttpServletRequest request) {
		proxyService.delete(path, reqPara, request.getRemoteAddr(), "DELETE");
	}
	
}
