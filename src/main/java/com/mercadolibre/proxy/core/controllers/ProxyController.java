package com.mercadolibre.proxy.core.controllers;

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
	public String query(@WildcardParam String path, @RequestParam MultiValueMap<String, String> requestParameter, HttpServletRequest request) {
		return proxyService.query(path, requestParameter, request.getRemoteAddr(), "GET").getValid();
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
	public String update(@WildcardParam String path, @RequestParam MultiValueMap<String, String> requestParameters, @RequestBody Object request, HttpServletRequest requestIpOrigin) {
			return proxyService.update(path, requestParameters, requestIpOrigin.getRemoteAddr(), "PUT", request);
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
		return new ResponseEntity<>(proxyService.create(path, requestParameters, request, "POST", requestIpOrigin.getRemoteAddr()),HttpStatus.OK);
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
	public String delete(@WildcardParam String path, @RequestParam MultiValueMap<String, String> requestParameters, @RequestBody Object request, HttpServletRequest requestIpOrigin) {
		return proxyService.delete(path, requestParameters, requestIpOrigin.getRemoteAddr(), "DELETE", request);
	}
	
}
