package com.sumscope.cdh.web.interceptor;


public class SecurityException extends  Exception{
    private static final String ERROR_INFO = "没登录或者session过期或者没权限";
    public SecurityException(){
        super(ERROR_INFO);
    }
    public SecurityException(String message){
        super(message);
    }
}
