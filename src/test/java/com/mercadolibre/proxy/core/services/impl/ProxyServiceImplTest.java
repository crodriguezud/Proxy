package com.mercadolibre.proxy.core.services.impl;

import com.mercadolibre.proxy.core.repositories.ParameterRepository;
import com.mercadolibre.proxy.core.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.client.RestTemplate;

class ProxyServiceImplTest {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    Environment env;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    ParameterRepository parameterRepository;

    @Autowired
    ProxyServiceImpl proxyServiceImpl = new ProxyServiceImpl(restTemplate, requestRepository, parameterRepository, env, mongoOperations);



}