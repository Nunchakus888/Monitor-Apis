package com.sumscope.cdh.web.domain;

/**
 * Created by Roidder on 2016/10/10.
 */
public class UserUsageApiList {
    private String apiName;
    private String countSql;
    private String countError;
    private int countApi;

    public String getCountSql() {
        return countSql;
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }

    public String getCountError() {
        return countError;
    }

    public void setCountError(String countError) {
        this.countError = countError;
    }

    public int getCountApi() {
        return countApi;
    }

    public void setCountApi(int countApi) {
        this.countApi = countApi;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public int getApiRate() {
        return countApi;
    }

    public void setApiRate(int apiRate) {
        this.countApi = apiRate;
    }
}
