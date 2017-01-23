package com.sumscope.cdh.web.service;

import com.sumscope.cdh.sumscopezk4j.*;
import com.sumscope.cdh.sumscopezk4j.zookeeper.ZookeeperInstance;
import com.sumscope.cdh.web.domain.EchoCode;
import com.sumscope.cdh.web.domain.JsonObj;
import com.sumscope.cdh.web.domain.ResultObj;
import com.sumscope.cdh.sumscopezk4j.zookeeper.Visualable;
import com.sumscope.cdh.sumscopezk4j.zookeeper.VisualableImpl;
import com.sumscope.cdh.sumscopezk4j.zookeeper.ZookeeperNode;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by wenshuai.li on 2016/10/14.
 */
@Component
@Configuration
public class ZookeeperService {
    private ZookeeperAdminInstance zookeeperInstance;
    
    private ZookeeperInstance zkClient;
    
    private int version = -1;

    private Visualable visualable;
    @Value("${spring.zookeeper.connect}")
    private String connect;

    @Value("${spring.zookeeper.digest}")
    private String digest;

    @Value("${spring.zookeeper.monitorPath}")
    private String monitorPath;

    @Value("${spring.zookeeper.serviceKey}")
    private String serviceKey;

    @Autowired
    private NodeDeleteListener nodeDeleteListener;

    @PostConstruct
    public void init() throws Exception{
        zookeeperInstance = new ZookeeperAdminInstance(connect,digest);
        zookeeperInstance.connect(ZookeeperAdminInstance.SCHEME,digest);

        makeBranch();
        makeMonitorBranch();
        visualable = new VisualableImpl(zookeeperInstance);

        Notice notice = new ZKnotice(zookeeperInstance);
        notice.notice(serviceKey);

        nodeDeleteListener.setWatchedPath(notice.getMonitorRoot());
        zookeeperInstance.listenChild(monitorPath,nodeDeleteListener);
    }

    public JsonObj getZookeeperNodes(String path, int depth) throws InterruptedException {
        ZookeeperNode zookeeperNode = null;
        ResultObj resultObj = new ResultObj();
        JsonObj jsonObj = new JsonObj();
        try{
            zookeeperNode = new ZookeeperNode();
            String value = zookeeperInstance.getStringData(path);
            zookeeperNode.setValue(value);

            zookeeperNode.setPath(path);
            zookeeperNode.setName(path);

            visualable.visual(zookeeperNode,depth);

        }catch (Exception e){
            resultObj.setStatus(false);
            resultObj.setMsg(e.getMessage());
            e.printStackTrace();
        }
        jsonObj.setData(zookeeperNode);
        jsonObj.setResult(resultObj);
        return jsonObj;
    }

    public JsonObj createNode(String path, String name, String data) throws InterruptedException {
        return createNode(path,name,data,0);
    }

    public JsonObj createNode(String path, String name, String data, int nodeType) throws InterruptedException {
        ZookeeperNode zookeeperNode = null;
        ResultObj resultObj = new ResultObj();
        JsonObj jsonObj = new JsonObj();
        try{
            zookeeperInstance.createNode(path + "/" + name,data.getBytes());

            zookeeperNode = new ZookeeperNode();
            zookeeperNode.setName(name);
            zookeeperNode.setPath(path + "/" + name);
            zookeeperNode.setValue(data);
            zookeeperNode.setNodeType(nodeType);
        }catch (Exception e){
            resultObj.setStatus(false);
            resultObj.setMsg(e.getMessage());
            resultObj.setEchoCode(EchoCode.CREATE_ERROR);//1007
        }
        jsonObj.setData(zookeeperNode);
        jsonObj.setResult(resultObj);
        return jsonObj;
    }

    public JsonObj delNode(String path) throws InterruptedException {
        ResultObj resultObj = new ResultObj();
        JsonObj jsonObj = new JsonObj();

        try {
            if("/com/sumscope".equals(path)){
                throw new IllegalArgumentException("不能删除跟节点/com/sumcope");
            }
            zookeeperInstance.deleteNode(path,version);
        }catch (IllegalArgumentException e){
            resultObj.setStatus(false);
            resultObj.setMsg(e.getMessage());
            resultObj.setEchoCode(EchoCode.CAN_NOT_DELETE_ROOT);//0
        }catch (Exception e){
            resultObj.setStatus(false);
            resultObj.setMsg(e.getMessage());
            resultObj.setEchoCode(EchoCode.DELETE_ERROR);//1005
        }
        jsonObj.setResult(resultObj);

        return jsonObj;

    }

    public JsonObj updateData(String path, String name, String data) throws InterruptedException {
        ResultObj resultObj = new ResultObj();
        JsonObj jsonObj = new JsonObj();
        ZookeeperNode zookeeperNode = null;
        try{
            zookeeperInstance.setData(path,data.getBytes(),version);

            zookeeperNode = new ZookeeperNode();
            zookeeperNode.setName(name);
            zookeeperNode.setPath(path);
            zookeeperNode.setValue(data);

            jsonObj.setData(zookeeperNode);
        }catch (Exception e){
            resultObj.setStatus(false);
            resultObj.setMsg(e.getMessage());
            resultObj.setEchoCode(EchoCode.UPDATE_ERROR);//1006
        }
        jsonObj.setResult(resultObj);

        return jsonObj;
    }

    public boolean exists(String path){
        boolean flag = false;
        try {
            flag =  zookeeperInstance.exists(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private void makeBranch() throws InterruptedException {
        try{
            if(!exists("/com")){
                createNode("","com","com");
            }
            if(!exists("/com/sumscope")){
                createNode("/com","sumscope","sumscope");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    
    private void makeMonitorBranch() throws Exception {
        zkClient = new ZookeeperInstance(connect);
        zkClient.connect();
        try{
            if(!zkClient.exists("/monitor")){
                zkClient.createNode("/monitor","monitor".getBytes(), CreateMode.PERSISTENT);
            }
            if(!zkClient.exists("/monitor/cdh")){
                zkClient.createNode("/monitor/cdh","cdh".getBytes(),CreateMode.PERSISTENT);
            }
        }finally {
            zkClient.closeConnect();
        }
        
    }
}
