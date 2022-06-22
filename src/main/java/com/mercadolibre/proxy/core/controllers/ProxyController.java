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
	 * Is a method for petition type Get
	 * @param path Uri petition
	 * @param requestParameter Parameters Uri
	 * @param request Get value of ip origin
	 * @return Request original
	 * @exception
	 * @author Cristian Rodriguez - 21/06/2022
	 */
	@GetMapping("/**")
	public ResponseEntity<?> query(@WildcardParam String path, @RequestParam MultiValueMap<String, String> requestParameter, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(proxyService.query(path, requestParameter, request.getRemoteAddr(), "GET"), HttpStatus.OK);
		}catch(InvalidUriException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}catch(UnlimitedException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Is a method for petition type PUT
	 * @param path Uri petition
	 * @param requestParameters Parameters Uri
	 * @param request get JSon Object of body
	 * @param requestIpOrigin Get value of ip origin
	 * @return Request original
	 * @exception
	 * @author Cristian Rodriguez - 22/06/2022
	 */
	@PutMapping("/**")
	public ResponseEntity<?> update(@WildcardParam String path, @RequestParam MultiValueMap<String, String> requestParameters, @RequestBody Object request, HttpServletRequest requestIpOrigin) {
		try {
			proxyService.update(path, requestParameters, requestIpOrigin.getRemoteAddr(), "PUT", request);
			return new ResponseEntity<>("Successful", HttpStatus.OK);
		}catch(InvalidUriException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}catch(UnlimitedException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Is a method for petition type POST
	 * @param path Uri petition
	 * @param requestParameters Parameters Uri
	 * @param request get JSon Object of body
	 * @param requestIpOrigin Get value of ip origin
	 * @return Request original
	 * @exception
	 * @author Cristian Rodriguez - 22/06/2022
	 */
	@PostMapping(value="/**",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@WildcardParam String path, @RequestParam MultiValueMap<String, String> requestParameters, @RequestBody Object request,  HttpServletRequest requestIpOrigin) {
		try {
			return new ResponseEntity<>(proxyService.create(path, requestParameters, request, "POST", requestIpOrigin.getRemoteAddr()),HttpStatus.OK);
		}catch(InvalidUriException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}catch(UnlimitedException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Is a method for petition type DELETE
	 * @param path Uri petition
	 * @param requestParameters Parameters Uri
	 * @param request get JSon Object of body
	 * @param requestIpOrigin Get value of ip origin
	 * @return Request original
	 * @exception
	 * @author Cristian Rodriguez - 22/06/2022
	 */
	@DeleteMapping("/**")
	public ResponseEntity<?> delete(@WildcardParam String path, @RequestParam MultiValueMap<String, String> requestParameters, @RequestBody Object request, HttpServletRequest requestIpOrigin) {
		try {
			proxyService.delete(path, requestParameters, requestIpOrigin.getRemoteAddr(), "DELETE", request);
			return new ResponseEntity<>("Successful", HttpStatus.OK);
		}catch(InvalidUriException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}catch(UnlimitedException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
