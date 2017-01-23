package com.sumscope.cdh.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Roidder on 2016/11/3.
 */
public class BaseController {
    public void setResponseHeader(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        response.addHeader("Access-Control-Max-Age", "60");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Accept-Charset", "UTF-8");
    }
}
