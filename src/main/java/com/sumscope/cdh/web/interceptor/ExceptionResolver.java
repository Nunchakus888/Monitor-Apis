package com.sumscope.cdh.web.interceptor;

import com.sumscope.cdh.web.domain.EchoCode;
import com.sumscope.cdh.web.domain.JsonObj;
import com.sumscope.cdh.web.domain.ResultObj;
import com.sumscope.cdh.web.util.JsonUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by wenshuai.li on 2016/11/17.
 */
@Component
public class ExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if(ex instanceof MultipartException){
            ResultObj resultObj = new ResultObj();
            JsonObj jsonObj = new JsonObj();
            resultObj.setStatus(false);
            resultObj.setEchoCode(EchoCode.FILE_TOO_BIG);
            resultObj.setMsg(ex.getMessage());
            jsonObj.setResult(resultObj);
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.print(JsonUtil.writeValueAsString(jsonObj));
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                out.close();
            }
        }
        return null;
    }
}
