package com.mercadolibre.proxy.core.repositories;

import com.mercadolibre.proxy.core.documents.Parameter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ParameterRepository extends MongoRepository<Parameter, String> {
    @Query("{name : ?0}")
    List<Parameter> getParameterByName(String name);

}
