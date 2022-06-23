package com.mercadolibre.proxy.core.services.impl;

import com.mercadolibre.proxy.core.repositories.ParameterRepository;
import com.mercadolibre.proxy.core.repositories.RequestRepository;
import org.junit.jupiter.api.Test;
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

    @Test
    void query(){
        proxyServiceImpl.validate("https://api.mercadolibre.com/categories/MCO1648", "192.168.0.1", "GET");

    }

}