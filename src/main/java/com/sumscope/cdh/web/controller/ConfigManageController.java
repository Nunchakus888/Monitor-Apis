package com.sumscope.cdh.web.controller;

import com.sumscope.cdh.web.domain.JsonObj;
import com.sumscope.cdh.web.service.ZookeeperService;
import com.sumscope.cdh.web.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by wenshuai.li on 2016/10/14.
 */
@RestController
@RequestMapping(value = "/configManage", produces = "application/json; charset=UTF-8")
public class ConfigManageController {
    @Autowired
    private ZookeeperService zookeeperService;

    @RequestMapping(value = "/getChildren", method = RequestMethod.POST)
    public String getChildren(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestBody String query) throws InterruptedException {
        Map map = JsonUtil.json2Map(query);
        String path = (String)map.get("path");

        JsonObj jsonObj = zookeeperService.getZookeeperNodes(path,2);

        return JsonUtil.writeValueAsString(jsonObj);

    }

    @RequestMapping(value = "/createChild", method = RequestMethod.POST)
    public String createChild(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestBody String query) throws InterruptedException {
        Map map = JsonUtil.json2Map(query);
        String path = (String)map.get("path");
        String name = (String)map.get("name");
        String value = (String)map.get("value");

        JsonObj jsonObj = zookeeperService.createNode(path,name,value);

        return JsonUtil.writeValueAsString(jsonObj);

    }

    @RequestMapping(value = "/deleteChild", method = RequestMethod.POST)
    public String deleteChild(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestBody String query) throws InterruptedException {
        Map map = JsonUtil.json2Map(query);
        String path = (String)map.get("path");

        JsonObj jsonObj = zookeeperService.delNode(path);


        return JsonUtil.writeValueAsString(jsonObj);

    }

    @RequestMapping(value = "/updateChild", method = RequestMethod.POST)
    public String updateChild(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestBody String query) throws  InterruptedException {
        Map map = JsonUtil.json2Map(query);
        String path = (String)map.get("path");
        String name = (String)map.get("name");
        String value = (String)map.get("value");

        assert path != null;
        assert value != null;

        JsonObj jsonObj = zookeeperService.updateData(path,name,value);


        return JsonUtil.writeValueAsString(jsonObj);

    }
}
