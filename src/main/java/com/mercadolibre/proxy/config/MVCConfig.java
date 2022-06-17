package com.mercadolibre.proxy.config;

import com.mercadolibre.proxy.utils.WildcardParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Autowired
    private PathMatcher pathMatcher;  // or replace it with bean creation function

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new WildcardParam.Resolver(this.pathMatcher));
    }
}