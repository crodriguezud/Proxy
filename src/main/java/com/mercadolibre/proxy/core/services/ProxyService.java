package com.mercadolibre.proxy.core.services;

import org.springframework.util.MultiValueMap;

public interface ProxyService {

	public void create(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method);
	public void update(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method);
	public String query(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method);
	public void delete(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method);
	public long generateSequence(String seqName);
}
