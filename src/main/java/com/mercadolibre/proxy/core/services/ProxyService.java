package com.mercadolibre.proxy.core.services;

import org.springframework.util.MultiValueMap;

public interface ProxyService {

	public String create(String path, MultiValueMap<String, String> requestParameters, Object request, String method, String ipOrigin);
	public void update(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method, Object request);
	public String query(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method);
	public void delete(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method, Object request);
	public long generateSequence(String seqName);
}
