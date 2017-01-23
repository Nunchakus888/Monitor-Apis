package com.sumscope.cdh.web.service;

import com.sumscope.cdh.web.domain.*;
import com.sumscope.cdh.web.mapper.ApiUsageMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Roidder on 2016/10/10.
 */
public class ApiUsageService {

    @Autowired
    private ApiUsageMapper apiUsageMapper;

    public List<ApiUsageRate> queryApiUsageRate(Map map){
        List<ApiUsageRate> list = apiUsageMapper.queryApiUsageRate(map);
        return list;
    }

    public List<ApiUsageTakeTime> queryApiUsageTakeTime(Map map){
        List<ApiUsageTakeTime> list = apiUsageMapper.queryApiUsageTakeTime(map);
        return list;
    }

    public List<ApiUsageDetails> queryApiUsageDetails(Map map){
        List<ApiUsageDetails> list = apiUsageMapper.queryApiUsageDetails(map);
        return list;
    }

    public List<ApiUsageByUsersRate> queryApiUsageByUsersRate(){
        List<ApiUsageByUsersRate> list = apiUsageMapper.queryApiUsageByUsersRate();
        return list;
    }

    public List<UserUsageApiList> queryUserUsageApiList(Map map){
        List<UserUsageApiList> lists = apiUsageMapper.queryUserUsageApiList(map);
        return lists;
    }

    public List<ErrorDetails> queryErrorDetails(Map map){
        List<ErrorDetails> lists = apiUsageMapper.queryErrorDetails(map);
        return lists;
    }

    public ApiUsageMapper getApiUsageMapper() {
        return apiUsageMapper;
    }

    public void setApiUsageMapper(ApiUsageMapper apiUsageMapper) {
        this.apiUsageMapper = apiUsageMapper;
    }

    public int queryTotalSize(String id, Map map){
        return apiUsageMapper.queryTotalSize(id, map);
    }
}
