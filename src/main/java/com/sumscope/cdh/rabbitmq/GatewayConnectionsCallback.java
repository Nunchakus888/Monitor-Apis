package com.sumscope.cdh.rabbitmq;

import com.sumscope.cdh.web.util.JsonUtil;
import com.sumscope.cdh.web.util.TaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by wenshuai.li on 2016/12/21.
 */
@Component
public class GatewayConnectionsCallback extends BaseMQCallBack{
    private static final Logger logger = LoggerFactory.getLogger(GatewayConnectionsCallback.class);
    
    @Override
    public boolean processString(String s) {
        return process(s);
    }
    
    @Override
    public boolean processBytes(byte[] bytes) {
        return process(new String(bytes));
    }
    
    private boolean process(String s){
        Map map = JsonUtil.readValueNoException(s,Map.class);
        logger.info("GatewayConnectionsCallback:" + s);
        TaskExecutor.connectionsQueue.offer(map);
        return true;
    }
}
