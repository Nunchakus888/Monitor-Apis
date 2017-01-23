package com.sumscope.cdh.web.service;

import com.sumscope.cdh.rabbitmq.*;
import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.cdh.sumscopemq4j.ReceiverFactory;
import com.sumscope.cdh.sumscopemq4j.SenderFactory;
import com.sumscope.cdh.sumscopemq4j.MqReceiverCallback;
import com.sumscope.cdh.sumscopemq4j.Receiver;
import com.sumscope.cdh.sumscopemq4j.Sender;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by wenshuai.li on 2016/11/22.
 */
@Service
public class RabbitmqService {


    @Value("${rabbitmq_host}")
    private String host;

    @Value("${rabbitmq_port}")
    private int port = 5672;

    @Value("${rabbitmq_requestedHeartbeat}")
    private int heartbeat;

    @Value("${sender.exchange_name}")
    private String senderExchangeName;

    private String receiverExchangeName="cdh.realtime.monitor.exception.fanout,cdh.realtime.monitor.biz.fanout," +
            "cdh.realtime.monitor.autosys.fanout,cdh.realtime.monitor.connections.fanout,cdh.realtime.monitor.throughput.fanout";

    private Sender[] senders = null;

    @Autowired
    private BizCallback bizCallback;

    @Autowired
    private ExceptionCallback exceptionCallback;

    @Autowired
    private AutosysCallback autosysCallback;
    
    @Autowired
    private GatewayConnectionsCallback gatewayConnectionsCallback;

    @Autowired
    private GatewayThroughputCallback gatewayThroughputCallback;
    
    @PostConstruct
    public void init(){
        initSenders(initCreateOptions(senderExchangeName));
        initReceivers(initCreateOptions(receiverExchangeName));
    }

    private CreateOptions[] initCreateOptions(String config){
        String[] exchangeNames = StringUtils.split(config,",");
        CreateOptions[] createOptions = new CreateOptions[exchangeNames.length];

        for(int i=0;i<createOptions.length;i++){
            CreateOptions createOption = new CreateOptions();
            createOption.setSingleConnection(true);
            createOption.setHost(host);
            createOption.setPort(port);
            createOption.setRequestedHeartbeat(heartbeat);

            createOption.setExchangeName(exchangeNames[i]);
            createOption.setSenderType(CreateOptions.SenderType.FANOUT);
            createOptions[i] = createOption;
        }
        return createOptions;
    }

    private void initSenders(CreateOptions[] createOptions){
        try {
            senders = SenderFactory.newSenders(createOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initReceivers(CreateOptions[] createOptions){
        MqReceiverCallback[] callbacks = new MqReceiverCallback[5];
        callbacks[0] = exceptionCallback;
        callbacks[1] = bizCallback;
        callbacks[2] = autosysCallback;
        callbacks[3] = gatewayConnectionsCallback;
        callbacks[4] = gatewayThroughputCallback;
        try {
            for(int i=0;i<createOptions.length;i++){
                Receiver receiver = ReceiverFactory.newReceiver(createOptions[i],callbacks[i]);
                receiver.receive();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Sender sender,String message){
        try {
            sender.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Sender getSender(int index){
        return senders[index];
    }
    public Sender[] getSenders() {
        return senders;
    }

    public void setSenders(Sender[] senders) {
        this.senders = senders;
    }
}
