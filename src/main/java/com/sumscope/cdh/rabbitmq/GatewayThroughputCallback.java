package com.sumscope.cdh.rabbitmq;

import com.sumscope.cdh.web.util.JsonUtil;
import com.sumscope.cdh.web.util.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by wenshuai.li on 2016/12/21.
 */
@Component
public class GatewayThroughputCallback extends BaseMQCallBack{
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
        logger.info("GatewayThroughputCallback:" + s);
        TaskExecutor.throughputQueue.offer(map);
        return true;
    }
    
}
