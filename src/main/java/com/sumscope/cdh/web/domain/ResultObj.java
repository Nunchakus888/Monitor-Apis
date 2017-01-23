package com.sumscope.cdh.web.domain;

/**
 * Created by wenshuai.li on 2016/10/17.
 */
import java.io.Serializable;

public class ResultObj implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -9154388667003336179L;

    private boolean status = true;

    private String msg;

    private int echoCode;

    public ResultObj() {

    }

    public ResultObj(boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getEchoCode() {
        return echoCode;
    }

    public void setEchoCode(int echoCode) {
        this.echoCode = echoCode;
    }
}
