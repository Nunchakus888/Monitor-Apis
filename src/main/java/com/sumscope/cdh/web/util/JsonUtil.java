package com.sumscope.cdh.web.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshuai.li on 2015/10/27.
 */
public class JsonUtil {
    public static String writeValueAsString(Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public static <T> T readValue(String content, Class<T> valueType) throws IOException{
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
            return mapper.readValue(content,valueType);
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T readValueNoException(String content, Class<T> valueType){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
            return mapper.readValue(content,valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T jacksonToCollection(String src,Class<?> collectionClass, Class<?>... valueType) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping();
        JavaType javaType= objectMapper.getTypeFactory().constructParametricType(collectionClass, valueType);
        return (T)objectMapper.readValue(src, javaType);
    }

    public static Map<String, Object> json2Map(String result) throws SecurityException {
        try {
            return JsonUtil.readValue(result, HashMap.class);
        } catch (Exception ex) {
            throw new SecurityException("json parse error");
        }
    }
}
