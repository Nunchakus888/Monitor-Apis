package com.sumscope.cdh.web.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenshuai.li on 2015/11/9.
 */
public class RestUtil {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    //public static final MultiValueMap<String, String> HEADERS_MAP;
    //public static final MultiValueMap<String, String> POST_HEADERS_MAP;

    /*static {
        HEADERS_MAP = new LinkedMultiValueMap<String, String>();
        //HEADERS_MAP.put("Accept", Arrays.asList("application/json;charset=UTF-8"));

        POST_HEADERS_MAP = new LinkedMultiValueMap<String, String>();
        //POST_HEADERS_MAP.put("Accept", Arrays.asList("application/json;charset=UTF-8"));
        //POST_HEADERS_MAP.put("Content-Type", Arrays.asList(MediaType.MULTIPART_FORM_DATA_VALUE));
    }*/

    public static <T> T getForObject(String url, Class<T> responseType, Map<String, Object> params) {
        resetCnMessageConverter(REST_TEMPLATE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        ResponseEntity<T> response = REST_TEMPLATE.exchange(builder.build().toUriString(), HttpMethod.GET, null, responseType);
        return response.getBody();
    }

    public static <T> T postWithParams(String url, Class<T> responseType, Map<String, String> params) {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<String, String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestParams.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(requestParams, null);
        ResponseEntity<T> response = REST_TEMPLATE.exchange(url, HttpMethod.POST, requestEntity, responseType);
        return response.getBody();
    }

    public static Map<String, Object> getHeaderAndBody(String url, Object object, Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        ResponseEntity response = REST_TEMPLATE.exchange(builder.build().toUriString(), HttpMethod.GET, null, String.class);
        HttpHeaders headers = response.getHeaders();

        Map<String, Object> map = new HashMap();
        map.put("headers", headers);
        map.put("bodys", response);
        return map;
    }

    public static void delete(String url) {
        REST_TEMPLATE.delete(url);
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getSessionIdFromCookie(ServerHttpRequest request){
        String sessionId = null;
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            List<String> cookies = servletRequest.getHeaders().get("cookie");
            if (cookies != null && !cookies.isEmpty()) {
                for (String cookie : cookies) {
                    if (cookie.contains("SESSION")) {
                        sessionId = cookie.replace("SESSION=", "");
                        break;
                    }
                }
            }
        }
        return sessionId;
    }
    private static void resetCnMessageConverter(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        HttpMessageConverter<?> converterTarget = null;
        for (HttpMessageConverter<?> item : converterList) {
            if (item.getClass() == StringHttpMessageConverter.class) {
                converterTarget = item;
                break;
            }
        }


        if (converterTarget != null) {
            converterList.remove(converterTarget);
        }
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converterList.add(converter);

        converterList.add(new FormHttpMessageConverter());
        converterList.add(new MappingJackson2HttpMessageConverter());
    }
}
