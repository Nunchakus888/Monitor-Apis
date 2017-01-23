
package com.sumscope.cdh.web;

import com.sumscope.cdh.sumscopezk4j.ZookeeperAdminInstance;
import com.sumscope.cdh.sumscopezk4j.zookeeper.Visualable;
import com.sumscope.cdh.sumscopezk4j.zookeeper.VisualableImpl;
import com.sumscope.cdh.sumscopezk4j.zookeeper.ZookeeperNode;
import com.sumscope.cdh.web.util.JsonUtil;
import org.apache.zookeeper.KeeperException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by wenshuai.li on 2016/10/13.
 */


public class ZookeeperInstanceTest {
    ZookeeperAdminInstance zookeeperInstance;

    String digest = "super:cdh+123";
    @Before
    public void init() throws Exception {
        String host = "172.16.66.128:2181";
        //String host = "172.16.66.128:2181";
        zookeeperInstance = new ZookeeperAdminInstance(host,digest);

        //zookeeperInstance.connect(ZookeeperAdminInstance.SCHEME,digest);
        zookeeperInstance.connect(ZookeeperAdminInstance.SCHEME,digest);
    }

    @Test
    public void getChildren() throws KeeperException, InterruptedException {


        ZookeeperNode zookeeperNode = new ZookeeperNode();
        zookeeperNode.setPath("/com/sumscope");

        Visualable visualable = new VisualableImpl(zookeeperInstance);
        List<ZookeeperNode> list = visualable.visual(zookeeperNode,100);
        String aaa = JsonUtil.writeValueAsString(zookeeperNode);
        System.out.println(aaa);
        zookeeperInstance.closeConnect();
    }

    @Test
    public void createNode() throws KeeperException, InterruptedException {

        try{
            byte [] data = "12133".getBytes();
            String result = zookeeperInstance.createNode("/monitor", data);
            System.out.println(result);
            System.out.println("4、--------create znode ok-----------\n");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            zookeeperInstance.closeConnect();
        }
    }



    @Test
    public void deleteNode() throws KeeperException, InterruptedException {


        try{
            zookeeperInstance.deleteNode("/monitor",-1);
        }finally {
            zookeeperInstance.closeConnect();
        }
    }


    @Test
    public void getData() throws KeeperException, InterruptedException {
        try{
            byte[] data =  zookeeperInstance.getData("/monitor/cdh");
            System.out.println(new String(data));
        }finally {
            zookeeperInstance.closeConnect();
        }
    }

    @Test
    public void setData() throws KeeperException, InterruptedException {
        try{
            zookeeperInstance.setData("/com/sumscope/java","123003222".getBytes(),-1);
        }finally {
            zookeeperInstance.closeConnect();
        }
    }
}
