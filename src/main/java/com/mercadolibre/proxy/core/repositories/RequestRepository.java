package com.mercadolibre.proxy.core.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mercadolibre.proxy.core.entities.Request;

public interface RequestRepository extends MongoRepository<Request, Long> {

	@Query(value = "{ipOrigin: ?0}", count = true)
	public Long countIp(String ipOrigin);
	
	@Query(value = "{path: ?0}", count = true)
	public Long countPath(String path);
	
	@Query(value = "{method: ?0}", count = true)
	public Long countMethod(String method);
	
}
