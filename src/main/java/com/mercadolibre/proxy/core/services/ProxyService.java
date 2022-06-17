package com.mercadolibre.proxy.core.services;

import org.springframework.util.MultiValueMap;

public interface ProxyService {

	public void create();
	public void update();
	public String query(String path, MultiValueMap<String, String> reqPara);
	public void delete();
	
}
