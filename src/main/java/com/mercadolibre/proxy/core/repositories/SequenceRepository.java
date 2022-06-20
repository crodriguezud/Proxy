package com.mercadolibre.proxy.core.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mercadolibre.proxy.core.entities.Request;

public interface SequenceRepository extends MongoRepository<Request, Long> {

}
