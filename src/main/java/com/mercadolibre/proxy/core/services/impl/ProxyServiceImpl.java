package com.mercadolibre.proxy.core.services.impl;


import com.mercadolibre.proxy.core.documents.Request;
import com.mercadolibre.proxy.core.documents.Sequence;
import com.mercadolibre.proxy.core.exceptions.InvalidUriException;
import com.mercadolibre.proxy.core.exceptions.UnlimitedException;
import com.mercadolibre.proxy.core.repositories.RequestRepository;
import com.mercadolibre.proxy.core.repositories.SequenceRepository;
import com.mercadolibre.proxy.core.services.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class ProxyServiceImpl implements ProxyService {

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
	public String create(String path, MultiValueMap<String, String> reqPara, Object request, String method) {
		
		String app = path.split("/")[0];
		String route = env.getProperty("proxy.route."+app);
		int separator = path.indexOf("/");
		path = path.substring(separator+1, path.length());
		if (validate(route+path, "", method)) {
			
			UriComponents builder = UriComponentsBuilder.fromHttpUrl(route+path)
	                .queryParams(reqPara).build();
			
			return rest.postForObject(builder.toUriString(), request, String.class);
			
		}else {
			return "";
		}
		
	}

	@Override
	public void update(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method) {
		
		String app = path.split("/")[0];
		String route = env.getProperty("proxy.route."+app);
		int separator = path.indexOf("/");
		path = path.substring(separator+1, path.length());
		if (validate(route+path, ipOrigin, method)) {
			
			UriComponents builder = UriComponentsBuilder.fromHttpUrl(route+path)
	                .queryParams(reqPara).build();
			
			rest.put(builder.toUriString(), String.class);
			
		}
		
	}

	@Override
	public String query(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method) {

		if (path.isBlank() || !path.contains("/") ){
			throw new InvalidUriException("Uri Invalida");
		}
		String app = path.split("/")[0];
		String route = env.getProperty("proxy.route." + app);
		int separator = path.indexOf("/");
		path = path.substring(separator + 1, path.length());
		if (validate(route + path, ipOrigin, method)) {

			UriComponents builder = UriComponentsBuilder.fromHttpUrl(route + path)
					.queryParams(reqPara).build();

			return rest.getForObject(builder.toUriString(), String.class);

		} else {
			return "";
		}
	}

	@Override
	public void delete(String path, MultiValueMap<String, String> reqPara, String ipOrigin, String method) {
		
		String app = path.split("/")[0];
		String route = env.getProperty("proxy.route."+app);
		int separator = path.indexOf("/");
		path = path.substring(separator+1, path.length());
		if (validate(route+path, ipOrigin, method)) {
			
			UriComponents builder = UriComponentsBuilder.fromHttpUrl(route+path)
	                .queryParams(reqPara).build();
			
			rest.delete(builder.toUriString(), String.class);
			
		}
		
	}
	
	public boolean validate(String route, String ipOrigin, String method) {
		try {
			long countIp = req.countIp(ipOrigin);
			long countPath = req.countPath(route);
			long countMethod = req.countMethod(method);

			if(countIp >= Long.valueOf(env.getProperty("proxy.max.ip")) || countPath >= Long.valueOf(env.getProperty("proxy.max.path")) || countMethod >= Long.valueOf(env.getProperty("proxy.max.method"))){
				throw new UnlimitedException("Limite de transacciones alcanzado");
			}

			if (countIp < Long.valueOf(env.getProperty("proxy.max.ip")) && countPath < Long.valueOf(env.getProperty("proxy.max.path")) && countMethod < Long.valueOf(env.getProperty("proxy.max.method"))) {

				Request reqData = new Request();
				reqData.setId(generateSequence("request_sequence"));
				reqData.setIpOrigin(ipOrigin);
				reqData.setPath(route);
				reqData.setMethod(method);
				req.insert(reqData);

				return true;

			} else {
				return false;
			}
		} catch (UncategorizedMongoDbException e) {
			System.out.println("MongoDB Server is Down");
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
