package com.mercadolibre.proxy.core.services;

import com.mercadolibre.proxy.core.dtos.ResponseGet;
import org.springframework.util.MultiValueMap;

public interface ProxyService {

	public String create(String path, MultiValueMap<String, String> requestParameters, Object request, String method, String ipOrigin);
	public String update(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method, Object request);
	public ResponseGet query(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method);
	public String delete(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method, Object request);
	public long generateSequence(String seqName);
}
