package com.sumscope.cdh.web.domain;

/**
 * Created by wenshuai.li on 2016/10/28.
 */
public class JsonObj {
    private Object data;
    private ResultObj result;

    public JsonObj(){

    }

    public JsonObj(ResultObj result){
        this.result = result;
    }
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResultObj getResult() {
        return result;
    }

    public void setResult(ResultObj result) {
        this.result = result;
    }
}
