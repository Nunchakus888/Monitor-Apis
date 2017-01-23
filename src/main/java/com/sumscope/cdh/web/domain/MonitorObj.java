package com.sumscope.cdh.web.domain;

/**
 * Created by wenshuai.li on 2016/11/23.
 */
public class MonitorObj {
    private String messageT;
    private Object data;

    public MonitorObj(){

    }

    public MonitorObj(String messageT){
        this.messageT = messageT;
    }

    public String getMessageT() {
        return messageT;
    }

    public void setMessageT(String messageT) {
        this.messageT = messageT;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
