package com.mercadolibre.proxy.core.controllers;

import com.mercadolibre.proxy.core.documents.Parameter;
import com.mercadolibre.proxy.core.exceptions.InvalidUriException;
import com.mercadolibre.proxy.core.exceptions.NoResultDocumentException;
import com.mercadolibre.proxy.core.repositories.ParameterRepository;
import com.mercadolibre.proxy.core.repositories.RequestRepository;
import com.mercadolibre.proxy.core.services.ProxyService;
import com.mercadolibre.proxy.core.services.impl.ProxyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProxyControllerTest {

    private final static String URI = "meli/categories/MCO1648";
    private final static String IP_ORIGIN = "127.0.0.1";
    private final static String METHOD = "GET";

    private ProxyController builder;

    @Mock
    private Environment env;

    @Mock
    MongoOperations mongoOperations;

    @Mock
    private MockHttpServletRequest request;

    private MultiValueMap<String, String> param;

    @InjectMocks
    private ProxyController proxyController;

    private ProxyService proxyService;

    @InjectMocks
    private ProxyServiceImpl proxyServiceImpl;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    ParameterRepository parameterRepository = Mockito.mock(ParameterRepository.class);

    @Mock
    CircuitBreakerFactory circuitBreakerFactory;



    @BeforeEach
    void setUp() {
        List<Parameter> list = new ArrayList<Parameter>();
        Parameter parameter = new Parameter();
        parameter.setName("meli");
        parameter.setValue("https://api.mercadolibre.com/");
        parameter.setId("1");
        list.add(parameter);
        Mockito.when(env.getProperty("proxy.max.ip")).thenReturn("10");
        Mockito.when(env.getProperty("proxy.max.path")).thenReturn("10");
        Mockito.when(env.getProperty("proxy.max.method")).thenReturn("10");
        Mockito.when(parameterRepository.getParameterByName("meli")).thenReturn(list);
        proxyServiceImpl = new ProxyServiceImpl(restTemplate, requestRepository, parameterRepository, env, mongoOperations);
        Mockito.when(proxyServiceImpl.generateSequence("request_sequence")).thenReturn(1l);
        proxyController = new ProxyController(proxyServiceImpl, circuitBreakerFactory);
        request.setRemoteAddr("127.0.0.1");


    }

    @Test
    @DisplayName("Test: request get Ok")
    void query_OK(){
        //Given
        param = new LinkedMultiValueMap<>();
        param.add("access_key", "73c2cbbed7274fdc21064749bcefbe41");
        when(proxyServiceImpl.query(URI, param, IP_ORIGIN, METHOD).getValid()).thenReturn(ArgumentMatchers.anyString());

        //Then
        String response = proxyController.query(URI, param, request);
        System.out.println("response"+response);
        //When
        assertEquals(response, "");
    }

    @Test
    @DisplayName("Test: request post Ok")
    void create_OK(){
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.example.com");
        request.setRemoteAddr("127.0.0.1");
        request.setRequestURI(URI);
        HashMap<String,String> parameters = new HashMap();
        parameters.put("name","morpheuws");
        parameters.put("job","leader");
        request.setParameters(parameters);
        when(proxyServiceImpl.create(URI, param, request , METHOD,IP_ORIGIN)).thenReturn(ArgumentMatchers.anyString());

        //Then
        String response = String.valueOf(proxyController.create(URI, param, parameters, request));

        //When
        assertEquals("", response);
    }

    @Test
    @DisplayName("Test: request put Ok")
    void update_OK(){
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.example.com");
        request.setRemoteAddr("127.0.0.1");
        request.setRequestURI(URI);
        HashMap<String,String> parameters = new HashMap();
        parameters.put("name","morpheus");
        parameters.put("job","zion resident");
        request.setParameters(parameters);
        when(proxyServiceImpl.update(URI, param, IP_ORIGIN , METHOD,request)).thenReturn(ArgumentMatchers.anyString());

        //Then
        String response = String.valueOf(proxyController.update(URI, param, parameters, request));

        //When
        assertEquals("", response);
    }

    @Test
    @DisplayName("Test: request delete Ok")
    void delete_OK(){
        //Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.example.com");
        request.setRemoteAddr("127.0.0.1");
        request.setRequestURI(URI);
        HashMap<String,String> parameters = new HashMap();
        parameters.put("name","morpheus");
        parameters.put("job","zion resident");
        request.setParameters(parameters);
        when(proxyServiceImpl.delete(URI, param, IP_ORIGIN , METHOD,request)).thenReturn(ArgumentMatchers.anyString());

        //Then
        String response = String.valueOf(proxyController.delete(URI, param, parameters, request));

        //When
        assertEquals("", response);
    }


    @Test
    @DisplayName("Test:list items cannot empty")
    void validateListNotEmpty() {

        //Given
        Exception exception = assertThrows(InvalidUriException.class, () -> {
            String path = "";
            proxyServiceImpl.query(path, param,  IP_ORIGIN, METHOD);
        });

        //When
        String actual = exception.getMessage();
        String expected = "Uri invalidate";

        //Then
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test:document Empty")
    void docuemntEmpty() {

        //Given
        Exception exception = assertThrows(NoResultDocumentException.class, () -> {
            String path = "/invalid/";
            proxyServiceImpl.query(path, param,  IP_ORIGIN, METHOD);
        });

        //When
        String actual = exception.getMessage();
        String expected = "No result in document";

        //Then
        assertEquals(actual, expected);
    }



}