package com.sumscope.cdh.rabbitmq;

import com.sumscope.cdh.web.service.RabbitmqService;
import com.sumscope.cdh.sumscopemq4j.MqReceiverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wenshuai.li on 2016/11/23.
 */
public abstract class BaseMQCallBack implements MqReceiverCallback {
    protected static final Logger logger = LoggerFactory.getLogger(BaseMQCallBack.class);
    @Autowired
    protected RabbitmqService rabbitmqService;

    @Override
    public boolean processString(String s) {
        return false;
    }

    @Override
    public boolean processBytes(byte[] bytes) {
        return false;
    }

    public void send(String message) throws Exception{
        rabbitmqService.send(rabbitmqService.getSender(0), message);
    }
}
