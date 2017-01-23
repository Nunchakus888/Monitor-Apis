package com.sumscope.cdh.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wenshuai.li on 2016/11/23.
 */
@Service("BizProcessor")
public class BizProcessor<BusinessInfo> implements MonitorProcessor<BusinessInfo> {
    @Autowired
    MonitorService monitorService;

    @Override
    public void process(BusinessInfo message) {
        try{
            monitorService.insertIntoBusinessInfo((com.sumscope.cdh.monitor.model.BusinessInfo) message);
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }

}
