package com.mercadolibre.proxy.core.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.proxy.core.services.ProxyService;
import com.mercadolibre.proxy.utils.WildcardParam;

@RestController
public class ProxyController {

	@Autowired
	private ProxyService proxyService;
	
	public ProxyController () {
	}
	
	
	@GetMapping("/**")
	public String query(@WildcardParam String path, @RequestParam MultiValueMap<String, String> reqPara, HttpServletRequest request) {
		return proxyService.query(path, reqPara, request.getRemoteAddr(), "GET");
		
	}
	
	@PutMapping("/**")
	public void update(@WildcardParam String path, @RequestParam MultiValueMap<String, String> reqPara, HttpServletRequest request) {
		proxyService.update(path, reqPara, request.getRemoteAddr(), "PUT");
	}
	
	@PostMapping("/**")
	public void create(@WildcardParam String path, @RequestParam MultiValueMap<String, String> reqPara, HttpServletRequest request) {
		proxyService.create(path, reqPara, request.getRemoteAddr(), "POST");
	}
	
	@DeleteMapping("/**")
	public void delete(@WildcardParam String path, @RequestParam MultiValueMap<String, String> reqPara, HttpServletRequest request) {
		proxyService.delete(path, reqPara, request.getRemoteAddr(), "DELETE");
	}
	
}
