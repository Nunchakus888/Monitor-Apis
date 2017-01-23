package com.sumscope.cdh.web.service;

import com.sumscope.cdh.monitor.model.BusinessInfo;
import com.sumscope.cdh.monitor.model.ExceptionInfo;
import com.sumscope.cdh.web.mapper.MonitorMapper;
import com.sumscope.cdh.web.util.DateUtil;
import com.sumscope.cdh.web.util.TaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Roidder on 2016/11/23.
 */
@Service
public class MonitorService<T> {

    @Autowired
    @Qualifier("BizProcessor")
    private MonitorProcessor bizProcessor;

    @Autowired
    @Qualifier("ExceptionProcessor")
    private MonitorProcessor exceptionProcessor;

    @Autowired
    @Qualifier("AutosysProcessor")
    private AutosysProcessor autosysProcessor;
    
    
    @Autowired
    @Qualifier("GatewayConnectionsProcessor")
    private GatewayConnectionsProcessor gatewayConnectionsProcessor;
    
    @Autowired
    @Qualifier("GatewayThroughputProcessor")
    private GatewayThroughputProcessor gatewayThroughputProcessor;
    
    @PostConstruct
    public void start() {
        handel(TaskExecutor.bizQueue, bizProcessor);
        handel(TaskExecutor.exceptionQueue, exceptionProcessor);
        handel(TaskExecutor.autosysQueue, autosysProcessor);
        handel(TaskExecutor.connectionsQueue, gatewayConnectionsProcessor);
        handel(TaskExecutor.throughputQueue, gatewayThroughputProcessor);
    }


    private void handel(BlockingQueue queue, MonitorProcessor processor) {
        TaskExecutor.executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Object obj = queue.take();
                        processor.process(obj);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Autowired
    MonitorMapper monitorMapper;

    public void insertIntoBusinessInfo(BusinessInfo businessInfo) {
        businessInfo.setTime(Long.parseLong(DateUtil.getDatetime(businessInfo.getTime())));
        monitorMapper.insertIntoBusinessInfo(businessInfo);
    }

    public void insertIntoExceptionInfo(ExceptionInfo exceptionInfo) {
        monitorMapper.insertIntoExceptionInfo(exceptionInfo);
    }

    public void insertConnections(Map map){
        monitorMapper.insertConnections(map);
    }
    
    public void insertThroughput(Map map){
        monitorMapper.insertThroughput(map);
    }
    public List<BusinessInfo> queryBusinessInfo(Map map){
        return monitorMapper.queryBusinessInfo(map);
    }

    public int queryTotalSize(Map map){
        return monitorMapper.queryTotalSize(map);
    }
    
    public List<Map> getGatewayConnections(Map map){
        return monitorMapper.getGatewayConnections(map);
    }
    
    public List<Map> getGatewayThroughput(Map map){
        return monitorMapper.getGatewayThroughput(map);
    }
    
    public List<Map> getGatewayConnectionsAll(Map map){
        return monitorMapper.getGatewayConnectionsAll(map);
    }
    
    public List<Map> getGatewayThroughputAll(Map map){
        return monitorMapper.getGatewayThroughputAll(map);
    }
    
}
