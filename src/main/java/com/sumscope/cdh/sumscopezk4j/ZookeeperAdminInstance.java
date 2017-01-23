package com.sumscope.cdh.sumscopezk4j;

import com.sumscope.cdh.sumscopezk4j.zkclient.ZkClientException;
import com.sumscope.cdh.sumscopezk4j.zookeeper.ZookeeperInstance;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenshuai.li on 2016/11/8.
 */
public class ZookeeperAdminInstance extends ZookeeperInstance {

    public static final String SCHEME = "digest";

    private String digest;

    private List<ACL> acls = new ArrayList();

    public ZookeeperAdminInstance(String connectString, String digest) throws NoSuchAlgorithmException{
        super(connectString);
        this.digest = digest;
        initACL();
    }

    private void initACL() throws NoSuchAlgorithmException {
        //添加第一个id，采用用户名密码形式
        Id id1 = new Id(SCHEME,DigestAuthenticationProvider.generateDigest(digest));
        ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);
        acls.add(acl1);
        //添加第二个id，所有用户可读权限
        ACL acl2 = new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE);
        acls.add(acl2);
    }
    /**
     * 在指定路径创建znode，并初始化数据
     * @param path　znode节点路径
     * @param data　数据
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String createNode(String path, byte[] data) throws KeeperException, InterruptedException {
        return super.zkClient.create(path, data, acls, CreateMode.PERSISTENT);
    }

    /**
     * 在指定路径设置数据
     * @param path　znode节点路径
     * @param data　重置数据
     * @param version　节点版本
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public Stat setData(String path, byte [] data, int version) throws KeeperException, InterruptedException {
        return super.zkClient.setData(path, data, version);
    }


    /**
     * 删除节点
     *
     * @param path　znode节点路径
     * @param version　节点版本
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void deleteNode(String path, int version) throws InterruptedException, KeeperException {
        List<String> children = super.zkClient.getChildren(path,true);
        for(String child : children){
            String childPath = path + "/" + child;
            deleteNode(childPath,version);
        }
        try{
            super.zkClient.delete(path,-1);
        }catch(ZkClientException e){
            //ignore
        }
    }


}
