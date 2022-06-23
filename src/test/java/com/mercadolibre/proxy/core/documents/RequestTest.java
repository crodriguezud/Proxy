package com.mercadolibre.proxy.core.documents;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

class RequestTest {

    @Autowired
    Request request = new Request();

    @BeforeEach
    void setup(){
        Mockito.when(request.getId()).thenReturn(1l);
        Mockito.when(request.getIpOrigin()).thenReturn("192.168.0.10");
        Mockito.when(request.getPath()).thenReturn("https://api.mercadolibre.com/categories/MCO1648");
        Mockito.when(request.getMethod()).thenReturn("GET");
    }



}