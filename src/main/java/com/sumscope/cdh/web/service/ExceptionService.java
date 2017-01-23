package com.sumscope.cdh.web.service;

import com.sumscope.cdh.monitor.model.ExceptionInfo;
import com.sumscope.cdh.web.domain.JsonObj;
import com.sumscope.cdh.web.domain.ResultObj;
import com.sumscope.cdh.web.mapper.MonitorMapper;
import com.sumscope.cdh.web.util.DateUtil;
import com.sumscope.cdh.web.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by wenshuai.li on 2015/11/19.
 */
@Component
public class ExceptionService extends SimpleMappingExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {

        ModelAndView mv = new ModelAndView();
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        try {

            ResultObj resultObj = new ResultObj();
            resultObj.setStatus(false);
            resultObj.setMsg(ex.getMessage());

            response.getWriter().write(JsonUtil.writeValueAsString(new JsonObj(resultObj)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mv;
    }

    @Autowired
    MonitorMapper monitorMapper;
    public void insertIntoExceptionInfo(ExceptionInfo exceptionInfo){
        exceptionInfo.setTime(Long.parseLong(DateUtil.getDatetime(exceptionInfo.getTime())));
        monitorMapper.insertIntoExceptionInfo(exceptionInfo);
    }

    public List<ExceptionInfo> queryExceptionInfo(Map map){
        return (List<ExceptionInfo>) monitorMapper.queryExceptionInfo(map);
    }

    public int queryTotalSize (Map map){
        return monitorMapper.queryTotalSize(map);
    }

}
