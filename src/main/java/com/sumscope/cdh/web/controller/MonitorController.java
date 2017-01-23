package com.sumscope.cdh.web.controller;

import com.sumscope.cdh.monitor.model.BusinessInfo;
import com.sumscope.cdh.web.service.BizProcessor;
import com.sumscope.cdh.web.service.ExceptionProcessor;
import com.sumscope.cdh.web.service.ExceptionService;
import com.sumscope.cdh.web.service.MonitorService;
import com.sumscope.cdh.web.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by wenshuai.li on 2016/4/6.
 */

@RestController
@RequestMapping(value = "/monitor", produces = "application/json; charset=UTF-8")
public final class MonitorController extends BaseController {
    @Autowired
    private ExceptionService exceptionService;

    @Autowired
    private MonitorService monitorService;

    @RequestMapping(value = "/exceptionInfo", method = RequestMethod.POST)
    public String exceptionInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody String query) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(query);
        int size = (int) map.get("size");
        int start = (Integer.parseInt(map.get("page").toString()) - 1) * size;
        map.put("page", start);
        map.put("table", "exception_info");
        List list = exceptionService.queryExceptionInfo(map);
        if(list.size() > 0){   //total size
            list.add(exceptionService.queryTotalSize(map));
        }
        return JsonUtil.writeValueAsString(list);
    }

    @RequestMapping(value = "/businessInfo", method = RequestMethod.POST)
    public String businessInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody String query) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(query);
        int size = (int) map.get("size");
        int start = (Integer.parseInt(map.get("page").toString()) - 1) * size;
        map.put("page", start);
        map.put("table", "business_info");
        List list = monitorService.queryBusinessInfo(map);
        if(list.size() > 0){
            list.add(monitorService.queryTotalSize(map));
        }
        return JsonUtil.writeValueAsString(list);
    }
    
    @RequestMapping(value = "/gateway/all", method = RequestMethod.POST)
    public String gatewayAll(HttpServletRequest request, HttpServletResponse response, @RequestBody String query){
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(query);
    
        List<Map> list1 = monitorService.getGatewayConnectionsAll(map);
        List<Map> list2 = monitorService.getGatewayThroughputAll(map);
        map.clear();
        map.put("connections",list1);
        map.put("throughput",list2);
    
        return JsonUtil.writeValueAsString(map);
    }
    
    @RequestMapping(value = "/gateway", method = RequestMethod.POST)
    public String gateway(HttpServletRequest request, HttpServletResponse response, @RequestBody String query){
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(query);
        
        List<Map> list1 = monitorService.getGatewayConnections(map);
        List<Map> list2 = monitorService.getGatewayThroughput(map);
        map.clear();
        map.put("connections",list1);
        map.put("throughput",list2);
        
        return JsonUtil.writeValueAsString(map);
    }
}