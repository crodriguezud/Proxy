package com.mercadolibre.proxy.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.mercadolibre.proxy.core.entities.Request;
import com.mercadolibre.proxy.core.repositories.RequestRepository;
import com.mercadolibre.proxy.core.services.ProxyService;

@Service
public class ProxyServiceImpl implements ProxyService {

	
	//private String route = "https://api.mercadolibre.com/";
	private String route = "http://api.countrylayer.com/";
	
	private RestTemplate rest;
	private RequestRepository req;
	
	public ProxyServiceImpl(RestTemplate rest, RequestRepository req) {
		this.rest = rest;
		this.req = req;
	}
	
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String query(String path, MultiValueMap<String, String> reqPara) {
		
		
		long count = req.countIp("1.1.1.1");
		
		if (count<10) {
		
			Request reqData = new Request();
			reqData.setId(count+1);
			reqData.setIpOrigin("1.1.1.1");
			
			req.insert(reqData);
			
			Map<String, String> variables = new HashMap<>();
			variables.put("path", path);
			
			UriComponents builder = UriComponentsBuilder.fromHttpUrl(route+path)
	                .queryParams(reqPara).build();
			
			
			return rest.getForObject(builder.toUriString(), String.class);
			
		}else {
			return "";
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

}
