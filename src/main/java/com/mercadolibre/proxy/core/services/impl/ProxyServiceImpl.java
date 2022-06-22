package com.mercadolibre.proxy.core.services.impl;


import com.mercadolibre.proxy.core.documents.Request;
import com.mercadolibre.proxy.core.documents.Sequence;
import com.mercadolibre.proxy.core.exceptions.InvalidUriException;
import com.mercadolibre.proxy.core.exceptions.MongoConnectionExpection;
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

	/**
	 * Is a method for petition type POST
	 * @param path Uri petition
	 * @param requestParameters Parameters Uri
	 * @param request get JSon Object of body
	 * @param method Petition type
	 * @param ipOrigin Get value of ip origin
	 * @return
	 * @author Cristian Rodriguez 22/06/2022
	 */
	@Override
	public String create(String path, MultiValueMap<String, String> requestParameters, Object request, String method, String ipOrigin) {
		
		String app = path.split("/")[0];
		String route = env.getProperty("proxy.route."+app);
		int separator = path.indexOf("/");
		path = path.substring(separator+1, path.length());
		validate(route+path, ipOrigin, method);

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(route+path)
				.queryParams(requestParameters).build();

		return rest.postForObject(builder.toUriString(), request, String.class);
		
	}

	/**
	 * Is a method for petition type PUT
	 * @param path Uri petition
	 * @param requestParameters Parameters Uri
	 * @param ipOrigin Get value of ip origin
	 * @param method Petition type
	 * @param request get JSon Object of body
	 * @author Cristian Rodriguez 22/06/2022
	 */
	@Override
	public void update(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method, Object request) {
		
		String app = path.split("/")[0];
		String route = env.getProperty("proxy.route."+app);
		int separator = path.indexOf("/");
		path = path.substring(separator+1, path.length());
		validate(route+path, ipOrigin, method);
			
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(route+path)
				.queryParams(requestParameters).build();

		rest.put(builder.toUriString(), request, String.class);
		
	}

	/**
	 *
	 * @param path Uri petition
	 * @param requestParameters Parameters Uri
	 * @param ipOrigin Get value of ip origin
	 * @param method Petition type
	 * @return
	 * @author Cristian Rodriguez 22/06/2022
	 */

	@Override
	public String query(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method) {

		if (path.isBlank() || !path.contains("/") ){
			throw new InvalidUriException("Uri invalidate");
		}
		String app = path.split("/")[0];
		String route = env.getProperty("proxy.route." + app);
		int separator = path.indexOf("/");
		path = path.substring(separator + 1, path.length());
		validate(route + path, ipOrigin, method);

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(route + path)
				.queryParams(requestParameters).build();

		return rest.getForObject(builder.toUriString(), String.class);

	}

	/**
	 * @param path Uri petition
	 * @param requestParameters Parameters Uri
	 * @param ipOrigin Get value of ip origin
	 * @param method Petition type
	 * @param request get JSon Object of body
	 * @author Cristian Rodriguez 22/06/2022
	 */
	@Override
	public void delete(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method, Object request) {
		
		String app = path.split("/")[0];
		String route = env.getProperty("proxy.route."+app);
		int separator = path.indexOf("/");
		path = path.substring(separator+1, path.length());
		validate(route+path, ipOrigin, method);
			
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(route+path)
				.queryParams(requestParameters).build();

		rest.delete(builder.toUriString(), String.class);

	}

	/**
	 * Method for validate limit of transactions
	 * @param route Path of petition
	 * @param ipOrigin Ip origin of petition
	 * @param method Petition type
	 * @author Cristian Rodriguez 22/06/2022
	 */
	public void validate(String route, String ipOrigin, String method) {
		try {
			long countIp = req.countIp(ipOrigin);
			long countPath = req.countPath(route);
			long countMethod = req.countMethod(method);

			if(countIp >= Long.valueOf(env.getProperty("proxy.max.ip")) || countPath >= Long.valueOf(env.getProperty("proxy.max.path")) || countMethod >= Long.valueOf(env.getProperty("proxy.max.method"))){
				throw new UnlimitedException("Limite de transacciones alcanzado");
			}else{
				Request reqData = new Request();
				reqData.setId(generateSequence("request_sequence"));
				reqData.setIpOrigin(ipOrigin);
				reqData.setPath(route);
				reqData.setMethod(method);
				req.insert(reqData);
			}
		} catch (UncategorizedMongoDbException e) {
			throw new MongoConnectionExpection("MongoDB Server is Down");
		}
	}

	/**
	 * Method for create sequence at documents
	 * @param sequenceName Name of sequence create
	 * @return Id for de document
	 * @author Cristian Rodriguez 22/06/2022
	 */
	@Override
	public long generateSequence(String sequenceName) {
		
		Query query = new Query(Criteria.where("id").is(sequenceName));
		Update update = new Update().inc("seq", 1);
		Sequence counter = mongoOperations.findAndModify(query, update, options().returnNew(true).upsert(true),Sequence.class);
		return !Objects.isNull(counter)?counter.getSeq():1;
	}
	
}
