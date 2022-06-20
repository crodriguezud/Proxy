package com.mercadolibre.proxy.core.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.mercadolibre.proxy.core.entities.Request;
import com.mercadolibre.proxy.core.entities.Sequence;
import com.mercadolibre.proxy.core.repositories.RequestRepository;
import com.mercadolibre.proxy.core.repositories.SequenceRepository;
import com.mercadolibre.proxy.core.services.ProxyService;

@Service
public class ProxyServiceImpl implements ProxyService {

	
	private String route = "https://api.mercadolibre.com/";
	//private String route = "http://api.countrylayer.com/";
	
	private RestTemplate rest;
	private RequestRepository req;
	private SequenceRepository seq;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private Environment env;
	
	public ProxyServiceImpl(RestTemplate rest, RequestRepository req, SequenceRepository seq) {
		this.rest = rest;
		this.req = req;
		this.seq = seq;
	}
	
	
	@Override
	public void create(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String query(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method) {
		
		
		if (validate(path, reqPara, ipOrigin, method)) {
		
			UriComponents builder = UriComponentsBuilder.fromHttpUrl(route+path)
	                .queryParams(reqPara).build();
			
			return rest.getForObject(builder.toUriString(), String.class);
			
		}else {
			return "";
		}
	}

	@Override
	public void delete(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean validate(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method){
		long countIp = req.countIp(ipOrigin);
		long countPath = req.countPath(route+path);
		long countMethod = req.countMethod(method);
		if (countIp<Long.valueOf(env.getProperty("proxy.max.ip")) && countPath <Long.valueOf(env.getProperty("proxy.max.path")) && countMethod <Long.valueOf(env.getProperty("proxy.max.method"))) {
		
			Request reqData = new Request();
			reqData.setId(generateSequence("request_sequence"));
			reqData.setIpOrigin(ipOrigin);
			reqData.setPath(route+path);
			reqData.setMethod(method);
			req.insert(reqData);
			
			return true;
			
		}else {
			return false;
		}
	}


	@Override
	public long generateSequence(String seqName) {
		
		Query query = new Query(Criteria.where("id").is(seqName));
		Update update = new Update().inc("seq", 1);
		Sequence counter = mongoOperations.findAndModify(query, update, options().returnNew(true).upsert(true),Sequence.class);
		return !Objects.isNull(counter)?counter.getSeq():1;
	}
	
}
