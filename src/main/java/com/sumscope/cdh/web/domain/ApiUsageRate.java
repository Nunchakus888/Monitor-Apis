package com.sumscope.cdh.web.domain;

import com.sumscope.cdh.web.util.DateUtil;

/**
 * Created by Roidder on 2016/10/10.
 */
public class ApiUsageRate {

    private String apiName;
    private int countApi;
    private String lastVisited;

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

    public int getCountApi() {
        return countApi;
    }

    public void setCountApi(int countApi) {
        this.countApi = countApi;
    }
}
