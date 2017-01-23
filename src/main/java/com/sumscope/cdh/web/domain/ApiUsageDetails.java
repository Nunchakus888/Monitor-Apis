package com.sumscope.cdh.web.domain;

/**
 * Created by Roidder on 2016/10/10.
 */
public class ApiUsageDetails {
    private String userName;
    private String msgDetail;
    private int sqlTakeTime;
    private int countSql;
    private int countError;
    private int rowCount;

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getCountError() {
        return countError;
    }

    public void setCountError(int countError) {
        this.countError = countError;
    }

    public int getCountSql() {
        return countSql;
    }

    public void setCountSql(int countSql) {
        this.countSql = countSql;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsgDetail() {
        return msgDetail;
    }

    public void setMsgDetail(String msgDetail) {
        this.msgDetail = msgDetail;
    }

    public int getSqlTakeTime() {
        return sqlTakeTime;
    }

    public void setSqlTakeTime(int sqlTakeTime) {
        this.sqlTakeTime = sqlTakeTime;
    }
}
