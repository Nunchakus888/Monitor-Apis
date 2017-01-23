package com.sumscope.cdh.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by wenshuai.li on 2016/12/21.
 */
@Service("GatewayConnectionsProcessor")
public class GatewayConnectionsProcessor implements MonitorProcessor {
    @Autowired
    private MonitorService monitorService;
    
    @Override
    public void process(Object message) {
        Map map = (Map)message;
        try{
            monitorService.insertConnections(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
}
