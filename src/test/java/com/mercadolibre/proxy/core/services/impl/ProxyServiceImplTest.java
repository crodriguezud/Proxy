package com.mercadolibre.proxy.core.services.impl;

import com.mercadolibre.proxy.core.repositories.ParameterRepository;
import com.mercadolibre.proxy.core.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

class ProxyServiceImplTest {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    ParameterRepository parameterRepository;

    @Autowired
    ProxyServiceImpl proxyServiceImpl = new ProxyServiceImpl(restTemplate, requestRepository, parameterRepository);



}