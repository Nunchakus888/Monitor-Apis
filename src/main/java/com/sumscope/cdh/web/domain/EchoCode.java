package com.sumscope.cdh.web.domain;

/**
 * Created by wenshuai.li on 2016/11/17.
 */
public interface EchoCode {
    //不能删除跟节点/com/sumcope
    int CAN_NOT_DELETE_ROOT = 0;
    //上传文件失败
    int UPLOAD_ERROR = 1000;

    //删除节点出错
    int DELETE_ERROR = 1005;
    //更新节点出错
    int UPDATE_ERROR = 1006;
    //添加节点出错
    int CREATE_ERROR = 1007;
    //添加节点出错
    int FILE_TOO_BIG = 1008;
}
