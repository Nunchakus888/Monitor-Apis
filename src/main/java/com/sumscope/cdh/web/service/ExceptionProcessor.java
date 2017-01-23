package com.sumscope.cdh.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wenshuai.li on 2016/11/23.
 */
@Service("ExceptionProcessor")
public class ExceptionProcessor<ExceptionInfo> implements MonitorProcessor<ExceptionInfo> {
    @Autowired
    MonitorService monitorService;

    @Override
    public void process(ExceptionInfo message) {
        try{
            monitorService.insertIntoExceptionInfo((com.sumscope.cdh.monitor.model.ExceptionInfo) message);
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }

}
