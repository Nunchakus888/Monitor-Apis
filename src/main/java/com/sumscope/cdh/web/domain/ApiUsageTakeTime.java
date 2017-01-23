package com.sumscope.cdh.web.domain;

import com.sumscope.cdh.web.util.DateUtil;

/**
 * Created by Roidder on 2016/10/10.
 */
public class ApiUsageTakeTime {
    private String apiName;
    private int sqlTakeTime;
    private String lastVisited;

    public int getSqlTakeTime() {
        return sqlTakeTime;
    }

    public void setSqlTakeTime(int sqlTakeTime) {
        this.sqlTakeTime = sqlTakeTime;
    }

    public String getLastVisited() {
        return DateUtil.y4m2d2h2m2s22h2m2s2(lastVisited);
    }

    public void setLastVisited(String lastVisited) {
        this.lastVisited = lastVisited;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public int getTakeTime() {
        return sqlTakeTime;
    }

    public void setTakeTime(int sqlTakeTime) {
        this.sqlTakeTime = sqlTakeTime;
    }
}
