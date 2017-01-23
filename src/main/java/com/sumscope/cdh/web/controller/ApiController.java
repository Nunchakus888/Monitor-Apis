package com.sumscope.cdh.web.controller;

import com.sumscope.cdh.web.domain.*;
import com.sumscope.cdh.web.service.ApiUsageService;
import com.sumscope.cdh.web.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by wenshuai.li on 2016/4/6.
 */

@RestController
@RequestMapping(value = "/monitor", produces = "application/json; charset=UTF-8")
public final class ApiController extends BaseController {
    @Autowired
    private ApiUsageService apiUsageService;

    @RequestMapping(value = "/apiUsageRate", method = RequestMethod.POST)
    public String apiUsageRate(HttpServletRequest request, HttpServletResponse response, @RequestBody String query) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(query);
        int size = (int) map.get("size");
        int start = (Integer.parseInt(map.get("page").toString()) - 1) * size;
        map.put("page", start);
        List<ApiUsageRate> list = apiUsageService.queryApiUsageRate(map);
        return JsonUtil.writeValueAsString(list);
    }

    @RequestMapping(value = "/sqlTaketime", method = RequestMethod.POST)
    public String apiUsageTakeTime(HttpServletRequest request, HttpServletResponse response, @RequestBody String query) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(query);
        int size = (int) map.get("size");
        int start = (Integer.parseInt(map.get("page").toString()) - 1) * size;
        map.put("page", start);
        List<ApiUsageTakeTime> list = apiUsageService.queryApiUsageTakeTime(map);
        return JsonUtil.writeValueAsString(list);
    }

    String APIDETAILSIZE_ID = "apiDetailsSize",ERRORDETAILSIZE_ID = "errorDetailsSize";

    @RequestMapping(value = "/apiUsagedetails", method = RequestMethod.POST)
    public String apiDetails(HttpServletRequest request, HttpServletResponse response, @RequestBody String query) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(query);
        int size = 20;
        int start = (Integer.parseInt(map.get("page").toString()) - 1) * size;
        map.put("page", start);
        map.put("size", size);
        List list = apiUsageService.queryApiUsageDetails(map);
        if(list.size() > 0){   //total size
            list.add(apiUsageService.queryTotalSize(APIDETAILSIZE_ID, map));
        }
        return JsonUtil.writeValueAsString(list);
    }

    @RequestMapping(value = "/userUsageApiRate")
    public String apiUsageByUsersRate(HttpServletRequest request, HttpServletResponse response) {
        setResponseHeader(request, response);
        List<ApiUsageByUsersRate> list = apiUsageService.queryApiUsageByUsersRate();
        return JsonUtil.writeValueAsString(list);
    }

    @RequestMapping(value = "/aUserUsageApiList", method = RequestMethod.POST)
    public String aUserUsageApiList(HttpServletRequest request, HttpServletResponse response, @RequestBody String query) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(query);
        int size = 20;
        int start = (Integer.parseInt(map.get("page").toString()) - 1) * size;
        map.put("page", start);
        map.put("size", size);
        List list = apiUsageService.queryUserUsageApiList(map);
        if(list.size() > 0){   //total size
            list.add(apiUsageService.queryTotalSize(APIDETAILSIZE_ID, map));
        }
        return JsonUtil.writeValueAsString(list);
    }

    @RequestMapping(value = "/error", method = RequestMethod.POST)
    public String errorDetails(HttpServletRequest request, HttpServletResponse response, @RequestBody String query) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(query);
        int size = 20;
        int start = (Integer.parseInt(map.get("page").toString()) - 1) * size;
        map.put("page", start);
        map.put("size", size);
        List list = apiUsageService.queryErrorDetails(map);
        if(list.size() > 0){   //total size
            list.add(apiUsageService.queryTotalSize(ERRORDETAILSIZE_ID, map));
        }
        return JsonUtil.writeValueAsString(list);
    }

    /*
    * List<someObject> list add the data of int/String
    */
    /*public void getMethod(List list, int content){
        try {
            Method method = list.getClass().getMethod("add", Object.class);
            method.invoke(list, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}