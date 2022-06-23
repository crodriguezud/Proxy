package com.mercadolibre.proxy.core.repositories;

import com.mercadolibre.proxy.core.documents.Sequence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SequenceRepository extends MongoRepository<Sequence, Long> {

}
