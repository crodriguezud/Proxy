package com.mercadolibre.proxy.core.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.mercadolibre.proxy.core.services.ProxyService;
import com.mercadolibre.proxy.utils.WildcardParam;

@RestController
public class ProxyController {

	//private String route = "https://api.mercadolibre.com/";
	private String route = "http://api.countrylayer.com/";
	
	private RestTemplate rest;
	
	@Autowired
	private ProxyService proxyService;
	
	public ProxyController (RestTemplate rest) {
		this.rest = rest;
	}
	
	
	@GetMapping("/**")
	public String query(@WildcardParam String path, @RequestParam MultiValueMap<String, String> reqPara) {
		return proxyService.query(path, reqPara);
		
	}
	
	@PutMapping
	public void update() {
		proxyService.update();
	}
	
	@PostMapping
	public void create() {
		proxyService.create();
	}
	
	@DeleteMapping
	public void delete() {
		proxyService.delete();
	}
	
}
