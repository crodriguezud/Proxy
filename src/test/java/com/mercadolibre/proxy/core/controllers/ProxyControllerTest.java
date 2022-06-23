package com.mercadolibre.proxy.core.controllers;

import com.mercadolibre.proxy.core.exceptions.InvalidUriException;
import com.mercadolibre.proxy.core.services.ProxyService;
import com.mercadolibre.proxy.core.services.impl.ProxyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProxyControllerTest {

    private final static String URI = "test/v2/name/CO";
    private final static String IP_ORIGIN = "127.0.0.1";
    private final static String METHOD = "GET";

    private ProxyController builder;


    private MockHttpServletRequest request;
    @Mock
    private MultiValueMap<String, String> param;

    @InjectMocks
    private ProxyController proxyController;

    private ProxyService proxyService;

    @InjectMocks
    private ProxyServiceImpl proxyServiceImpl;

    @BeforeEach
    void setUp() {
        param = new LinkedMultiValueMap<>();
        param.add("access_key", "73c2cbbed7274fdc21064749bcefbe41");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.example.com");
        request.setRemoteAddr("127.0.0.1");
        request.setRequestURI(URI);

    }

    @Test
    @DisplayName("Test: ")
    void query_OK(){
        //Given
        param = new LinkedMultiValueMap<>();
        param.add("access_key", "73c2cbbed7274fdc21064749bcefbe41");
        //when(proxyServiceImpl.query(URI, param, IP_ORIGIN, METHOD).getValid()).thenReturn(anyString());

        //Then
        //String response = proxyController.query(URI, param, request);

        //When
        //assertEquals("", response);
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


}