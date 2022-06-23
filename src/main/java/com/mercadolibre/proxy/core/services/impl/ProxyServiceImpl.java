package com.mercadolibre.proxy.core.services.impl;


import com.mercadolibre.proxy.core.documents.Parameter;
import com.mercadolibre.proxy.core.documents.Request;
import com.mercadolibre.proxy.core.documents.Sequence;
import com.mercadolibre.proxy.core.dtos.ResponseGet;
import com.mercadolibre.proxy.core.exceptions.InvalidUriException;
import com.mercadolibre.proxy.core.exceptions.MongoConnectionExpection;
import com.mercadolibre.proxy.core.exceptions.NoResultDocumentException;
import com.mercadolibre.proxy.core.exceptions.UnlimitedException;
import com.mercadolibre.proxy.core.repositories.ParameterRepository;
import com.mercadolibre.proxy.core.repositories.RequestRepository;
import com.mercadolibre.proxy.core.services.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class ProxyServiceImpl implements ProxyService {

	private RestTemplate restTemplate;
	private RequestRepository requestRepository;
	private ParameterRepository parameterRepository;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private Environment env;

	public ProxyServiceImpl(RestTemplate restTemplate, RequestRepository requestRepository, ParameterRepository parameterRepository) {
		this.restTemplate = restTemplate;
		this.requestRepository = requestRepository;
		this.parameterRepository = parameterRepository;
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
	@Cacheable(value = "proxy", key = "#path+#method+#requestParameters.toString()")
	public String create(String path, MultiValueMap<String, String> requestParameters, Object request, String method, String ipOrigin) {

		if (path.isBlank() || !path.contains("/") ){
			throw new InvalidUriException("Uri invalidate");
		}
		path = buildPath(path);
		validate(path, ipOrigin, method);

		return (String) sendRequest(method, requestParameters, path, null);
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
	@Cacheable(value = "proxy", key = "#path+#method+#requestParameters.toString()")
	public String update(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method, Object request) {

		if (path.isBlank() || !path.contains("/") ){
			throw new InvalidUriException("Uri invalidate");
		}
		path = buildPath(path);
		validate(path, ipOrigin, method);
			
		return (String) sendRequest(method, requestParameters, path, request);
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
	public ResponseGet query(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method) {

		if (path.isBlank() || !path.contains("/") ){
			throw new InvalidUriException("Uri invalidate");
		}
		path = buildPath(path);
		validate(path, ipOrigin, method);

		return (ResponseGet) sendRequest(method, requestParameters, path, null);

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
	@Cacheable(value = "proxy", key = "#path+#method+#requestParameters.toString()")
	public String delete(String path, MultiValueMap<String, String> requestParameters, String ipOrigin, String method, Object request) {

		if (path.isBlank() || !path.contains("/") ){
			throw new InvalidUriException("Uri invalidate");
		}
		path = buildPath(path);
		validate(path, ipOrigin, method);

		return (String) sendRequest(method, requestParameters, path, request);
	}

	@Cacheable(value="proxy", key="#method+#requestParameters+#path")
	public Object sendRequest(String method, MultiValueMap<String, String> requestParameters, String path, Object request){

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(path)
				.queryParams(requestParameters).build();
		ResponseGet responseGet= new ResponseGet();
		HttpEntity<Object> body = new HttpEntity<>(request);
		switch (method){
			case "GET":
				responseGet.setValid(restTemplate.getForObject(builder.toUriString(), String.class));
				return responseGet;
			case "POST":
				return restTemplate.postForObject(builder.toUriString(), request, String.class);
			case "DELETE":
				return restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, body, String.class).getBody();
			case "PUT":
				return restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, body, String.class).getBody();
			default:
				return "";
		}

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
			long countIp = requestRepository.countIp(ipOrigin);
			long countPath = requestRepository.countPath(route);
			long countMethod = requestRepository.countMethod(method);

			if(countIp >= Long.valueOf(env.getProperty("proxy.max.ip")) || countPath >= Long.valueOf(env.getProperty("proxy.max.path")) || countMethod >= Long.valueOf(env.getProperty("proxy.max.method"))){
				throw new UnlimitedException("Limite de transacciones alcanzado");
			}else{
				Request requestData = new Request();
				requestData.setId(generateSequence("request_sequence"));
				requestData.setIpOrigin(ipOrigin);
				requestData.setPath(route);
				requestData.setMethod(method);
				requestRepository.insert(requestData);
			}
		} catch (UncategorizedMongoDbException e) {
			throw new MongoConnectionExpection("MongoDB Server is Down");
		}
	}

	/**
	 * Method for build path to send request
	 * @param path path give in service
	 * @return
	 * @author Cristian Rodriguez 22/06/2022
	 */
	public String buildPath(String path){
		String app = path.split("/")[0];

		List<Parameter> route = parameterRepository.getParameterByName(app);

		if (route.isEmpty()){
			throw new NoResultDocumentException("No result in document");
		}

		int separator = path.indexOf("/");
		path = path.substring(separator + 1, path.length());
		return route.get(0).getValue() +path;
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
