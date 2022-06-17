package com.mercadolibre.proxy.utils;

import org.springframework.core.MethodParameter;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface WildcardParam {

    class Resolver implements HandlerMethodArgumentResolver {

        private PathMatcher pathMatcher;

        public Resolver(PathMatcher pathMatcher) {
            this.pathMatcher = pathMatcher;
        }

        @Override
        public boolean supportsParameter(MethodParameter methodParameter) {
            Annotation annotation = methodParameter.getParameterAnnotation(WildcardParam.class);
            return annotation != null;
        }

        @Override
        public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modeContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
            if (servletRequest == null) {
                return null;
            }
            String patternAttribute = (String) servletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            String mappingAttribute = (String) servletRequest.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            return this.pathMatcher.extractPathWithinPattern(patternAttribute, mappingAttribute);
        }
    }
}