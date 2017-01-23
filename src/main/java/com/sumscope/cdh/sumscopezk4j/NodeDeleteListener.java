package com.sumscope.cdh.sumscopezk4j;

import com.sumscope.cdh.sumscopezk4j.zkclient.ZkClientException;
import com.sumscope.cdh.sumscopezk4j.zkclient.listener.Listener;
import com.sumscope.cdh.web.domain.MonitorObj;
import com.sumscope.cdh.web.service.RabbitmqService;
import com.sumscope.cdh.web.util.JsonUtil;
import com.sumscope.cdh.sumscopemq4j.Sender;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumscope.cdh.monitor.model.HeartbeatInfo;
import java.net.SocketException;

/**
 * Created by wenshuai.li on 2016/11/22.
 */
@Service
public class NodeDeleteListener implements Listener {
    private String watchedPath;

    @Autowired
    private RabbitmqService rabbitmqService;
    
    @Autowired
    private com.sumscope.cdh.web.util.EmailUtil emailUtil;
    
    @Override
    public void listen(String path, Watcher.Event.EventType eventType, byte[] bytes) throws ZkClientException, SocketException {
        System.out.println(path + " " + eventType.name() + new String(bytes));
        Sender sender = rabbitmqService.getSender(0);
        MonitorObj monitorObj = new MonitorObj(HeartbeatInfo.MonitorType.HEARTBEAT.value());

        HeartbeatInfo heartbeatInfo = new HeartbeatInfo();
    
        if(path.startsWith(watchedPath)){
            String serviceKey = path.replace(watchedPath+"/","");
            heartbeatInfo.setServiceKey(serviceKey);
            if(eventType.NodeCreated.getIntValue() == eventType.getIntValue()){
                heartbeatInfo.setStatus(1);
    
                //emailUtil.sendNoticeEmail("process: " + serviceKey + " started");
            }else {
                emailUtil.sendNoticeEmail("process: " + serviceKey + " stoped");
            }
            
            monitorObj.setData(heartbeatInfo);
            rabbitmqService.send(sender, JsonUtil.writeValueAsString(monitorObj));
        }
    }

    public String getWatchedPath() {
        return watchedPath;
    }

    public void setWatchedPath(String watchedPath) {
        this.watchedPath = watchedPath;
    }

    public RabbitmqService getRabbitmqService() {
        return rabbitmqService;
    }

    public void setRabbitmqService(RabbitmqService rabbitmqService) {
        this.rabbitmqService = rabbitmqService;
    }
}
