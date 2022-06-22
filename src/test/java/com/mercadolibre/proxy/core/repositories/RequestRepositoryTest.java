package com.mercadolibre.proxy.core.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class RequestRepositoryTest {

    RequestRepository requestRepository = Mockito.mock(RequestRepository.class);

    @BeforeEach
    void setup(){
        Mockito.when(requestRepository.countIp("192.168.0.10")).thenReturn(10l);
        Mockito.when(requestRepository.countMethod("GET")).thenReturn(10l);
        Mockito.when(requestRepository.countPath("https://api.mercadolibre.com/categories/MCO1648")).thenReturn(10l);
    }

    @Test
    void countIp(){
        long rta = requestRepository.countIp("192.168.0.10");
        Assertions.assertEquals(rta, 10l);
    }

    @Test
    void countMethod(){
        long rta = requestRepository.countMethod("GET");
        Assertions.assertEquals(rta, 10l);
    }

    @Test
    void countPath(){
        long rta = requestRepository.countPath("https://api.mercadolibre.com/categories/MCO1648");
        Assertions.assertEquals(rta, 10l);
    }
}