package com.mercadolibre.proxy.core.repositories;

import com.mercadolibre.proxy.core.documents.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParameterRepository extends MongoRepository<Request, Long> {
}
