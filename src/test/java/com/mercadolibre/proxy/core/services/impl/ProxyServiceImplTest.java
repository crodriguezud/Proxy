package com.mercadolibre.proxy.core.services.impl;

import com.mercadolibre.proxy.core.repositories.RequestRepository;
import com.mercadolibre.proxy.core.repositories.SequenceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class ProxyServiceImplTest {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    SequenceRepository sequenceRepository;

    @Autowired
    ProxyServiceImpl proxyServiceImpl = new ProxyServiceImpl(restTemplate, requestRepository, sequenceRepository);

    @Test
    void query(){
        Boolean rta = proxyServiceImpl.validate("https://api.mercadolibre.com/categories/MCO1648", "192.168.0.1", "GET");
        Assertions.assertEquals(true, rta);
    }

}