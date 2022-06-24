package com.mercadolibre.proxy.core.repositories;

import com.mercadolibre.proxy.core.documents.Parameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

class ParameterRepositoryTest {

    ParameterRepository parameterRepository = Mockito.mock(ParameterRepository.class);

    @BeforeEach
    void setup(){
        List<Parameter> list = new ArrayList<Parameter>();
        Parameter parameter = new Parameter();
        parameter.setName("meli");
        parameter.setValue("https://api.mercadolibre.com/");
        parameter.setId("1");
        list.add(parameter);
        Mockito.when(parameterRepository.getParameterByName("meli")).thenReturn(list);
    }

    @Test
    void countIp(){
        String rta = parameterRepository.getParameterByName("meli").get(0).getValue();
        Assertions.assertEquals(rta, "https://api.mercadolibre.com/");
    }

}