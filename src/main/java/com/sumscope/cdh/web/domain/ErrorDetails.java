package com.sumscope.cdh.web.domain;

import com.sumscope.cdh.web.util.DateUtil;

/**
 * Created by Roidder on 2016/10/10.
 */
public class ErrorDetails {
    private int logId;
    private String logType;
    private String userName;
    private String className;
    private String method;
    private String createTime;
    private String msg;
    private String sqlRowCount;
    private String msgDetail;
    private String apiName;
    private String sqlColcount;
    private String dataSource;
    private String sqlTakeTime;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCreateTime() {
        return DateUtil.y4m2d2h2m2s22h2m2s2(createTime);
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSqlRowCount() {
        return sqlRowCount;
    }

    public void setSqlRowCount(String sqlRowCount) {
        this.sqlRowCount = sqlRowCount;
    }

    public String getMsgDetail() {
        return msgDetail;
    }

    public void setMsgDetail(String msgDetail) {
        this.msgDetail = msgDetail;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getSqlColcount() {
        return sqlColcount;
    }

    public void setSqlColcount(String sqlColcount) {
        this.sqlColcount = sqlColcount;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getSqlTakeTime() {
        return sqlTakeTime;
    }

    public void setSqlTakeTime(String sqlTakeTime) {
        this.sqlTakeTime = sqlTakeTime;
    }
}
