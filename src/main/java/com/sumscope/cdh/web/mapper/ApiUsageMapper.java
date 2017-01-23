package com.sumscope.cdh.web.mapper;

import com.sumscope.cdh.web.domain.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Roidder on 2016/10/10.
 */

public class ApiUsageMapper {

    @Autowired
    private SqlSessionTemplate dataServiceSessionTemplate;

    //api使用频率
    public List<ApiUsageRate> queryApiUsageRate(Map map){
        List<ApiUsageRate> list = dataServiceSessionTemplate.selectList("apiUsageRate", map);
        return list;
    }

    //api延时统计
    public List<ApiUsageTakeTime> queryApiUsageTakeTime(Map map){
        List<ApiUsageTakeTime> list = dataServiceSessionTemplate.selectList("apiUsageTakeTime", map);
        return list;
    }

    //某个api使用情况
    public List<ApiUsageDetails> queryApiUsageDetails(Map map){
        List<ApiUsageDetails> list = dataServiceSessionTemplate.selectList("aApiUsageDetails", map);
        return list;
    }

    //用户访问API次数排名
    public List<ApiUsageByUsersRate> queryApiUsageByUsersRate(){
        List<ApiUsageByUsersRate> list = dataServiceSessionTemplate.selectList("apiUsageByUsersRate");
        return list;
    }

    //某个用户访问的API列表
    public List<UserUsageApiList> queryUserUsageApiList(Map map){
        List<UserUsageApiList> list = dataServiceSessionTemplate.selectList("aUserUsageApiList", map);
        return list;
    }

    //Error
    public List<ErrorDetails> queryErrorDetails(Map map){
        List<ErrorDetails> list = dataServiceSessionTemplate.selectList("errorDetails", map);
        return list;
    }

    //totalPage
    public int queryTotalSize(String id, Map map){
        return dataServiceSessionTemplate.selectOne(id, map);
    }

    public SqlSessionTemplate getDataServiceSessionTemplate() {
        return dataServiceSessionTemplate;
    }

    public void setDataServiceSessionTemplate(SqlSessionTemplate dataServiceSessionTemplate) {
        this.dataServiceSessionTemplate = dataServiceSessionTemplate;
    }
}
