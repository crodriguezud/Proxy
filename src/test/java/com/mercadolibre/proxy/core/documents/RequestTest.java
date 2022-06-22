package com.mercadolibre.proxy.core.documents;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Test
    void getId(){
        long rta = request.getId();
        Assertions.assertEquals(1l, rta);
    }

    @Test
    void getMethod(){
        String rta = request.getMethod();
        Assertions.assertEquals("GET", rta);
    }

    @Test
    void getIpOrigin(){
        String rta = request.getIpOrigin();
        Assertions.assertEquals("192.168.0.10", rta);
    }

    @Test
    void getPath(){
        String rta = request.getPath();
        Assertions.assertEquals("https://api.mercadolibre.com/categories/MCO1648", rta);
    }

}