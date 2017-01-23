
package com.sumscope.cdh.web.mapper;

import com.sumscope.cdh.monitor.model.BusinessInfo;
import com.sumscope.cdh.monitor.model.ExceptionInfo;
import com.sumscope.cdh.monitor.model.gateway.ConnStatistics;
import com.sumscope.cdh.monitor.model.gateway.ThroughputStatistics;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Roidder on 2016/10/10.
 */

public class MonitorMapper {

    @Autowired
    private SqlSessionTemplate dataServiceSessionTemplate;

    public void insertIntoBusinessInfo(BusinessInfo businessInfo){
        dataServiceSessionTemplate.insert("insertIntoBusinessInfo", businessInfo);
    }

    public void insertIntoExceptionInfo(ExceptionInfo exceptionInfo){
        dataServiceSessionTemplate.insert("insertIntoExceptionInfo", exceptionInfo);
    }

    public List<BusinessInfo> queryBusinessInfo(Map map){
        return dataServiceSessionTemplate.selectList("queryBusinessInfo", map);
    }

    public List<ExceptionInfo> queryExceptionInfo(Map map){
        return dataServiceSessionTemplate.selectList("queryExceptionInfo", map);
    }

    public int queryTotalSize(Map map){
        return dataServiceSessionTemplate.selectOne("monitorDataSize", map);
    }

    public void insertConnections(Map map){
        dataServiceSessionTemplate.insert("insertConnections",map);
    }
    
    public void insertThroughput(Map map){
        dataServiceSessionTemplate.insert("insertThroughput",map);
    }
    
    public List<Map> getGatewayConnections(Map map){
        return dataServiceSessionTemplate.selectList("t_gateway_connections",map);
    }
    
    public List<Map> getGatewayThroughput(Map map){
        return dataServiceSessionTemplate.selectList("t_gateway_throughput",map);
    }
    
    public List<Map> getGatewayConnectionsAll(Map map){
        return dataServiceSessionTemplate.selectList("t_gateway_connections_all",map);
    }
    
    public List<Map> getGatewayThroughputAll(Map map){
        return dataServiceSessionTemplate.selectList("t_gateway_throughput_all",map);
    }
    
    public int createExceptionInfo(){
        return dataServiceSessionTemplate.update("createExceptionInfo");
    }

    public int createBusinessInfo(){
        return dataServiceSessionTemplate.update("createBusinessInfo");
    }

    public SqlSessionTemplate getDataServiceSessionTemplate() {
        return dataServiceSessionTemplate;
    }

    public void setDataServiceSessionTemplate(SqlSessionTemplate dataServiceSessionTemplate) {
        this.dataServiceSessionTemplate = dataServiceSessionTemplate;
    }
}
