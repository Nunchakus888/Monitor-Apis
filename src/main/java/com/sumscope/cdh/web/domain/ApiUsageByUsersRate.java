package com.sumscope.cdh.web.domain;

import com.sumscope.cdh.web.util.DateUtil;

/**
 * Created by Roidder on 2016/10/10.
 */
public class ApiUsageByUsersRate {
    private String userName;
    private int apiUsageByUserRate;
    private String lastVisited;

    public String getLastVisited() {
        return DateUtil.y4m2d2h2m2s22h2m2s2(lastVisited);
    }

    public void setLastVisited(String lastVisited) {
        this.lastVisited = lastVisited;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getApiUsageRage() {
        return apiUsageByUserRate;
    }

    public void setApiUsageRage(int apiUsageRage) {
        this.apiUsageByUserRate = apiUsageRage;
    }
}
