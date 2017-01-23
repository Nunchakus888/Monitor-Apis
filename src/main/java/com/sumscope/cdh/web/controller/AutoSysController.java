package com.sumscope.cdh.web.controller;

import com.sumscope.cdh.web.service.AutoSysService;
import com.sumscope.cdh.web.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by wenshuai.li on 2016/4/6.
 */

@RestController
@RequestMapping(value = "/autosys", produces = "application/json; charset=UTF-8")
public final class AutoSysController extends BaseController {
    @Autowired
    private AutoSysService autoSysService;

    @RequestMapping(value = "/jobInfo", method = RequestMethod.POST)
    public String jobInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody String query) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(query);
        int size = (int) map.get("size"),
                start = (Integer.parseInt(map.get("page").toString()) - 1) * size;
        map.put("page", start);
        map.put("size", size);
        List list = autoSysService.queryJobInfo(map);
        String[] triggerConfig = new String[]{"triggerName","taketime","startDetail","endDetail","status","triggerGroup","triggerDescription","startTime","endTime","nextFireTime","prevFireTime","priority","triggerState","triggerType","calendarName","misfireInstr","triggerJobData"};
        Map jobMap = null;
        Map emptyMap = new HashMap();
        List jobList = new ArrayList();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                List triggerList = new ArrayList();
                Map triggerMap = new HashMap();
                jobMap = JsonUtil.json2Map(JsonUtil.writeValueAsString(list.get(i)));
                for (String trigger : triggerConfig) {
                    triggerMap.put(trigger, jobMap.get(trigger));   //only trigger info
                    jobMap.remove(trigger);                         //only job info
                }
                triggerList.add(triggerMap);
                jobMap.put("nodes", triggerList);                   //job + nodes[trigger1]
                jobList.add(jobMap);//job[attr1, attr2, attr3, nodes[attr1, attr2, attr3]]
            }

            for (int i = 0; i < jobList.size(); i++) {
                for (int j = i + 1; j < jobList.size(); j++) {
                    if (((HashMap) jobList.get(i)).size() > 0) {
                        if (((HashMap) jobList.get(j)).size() > 0) {
                            if (((HashMap) jobList.get(i)).get("jobName").equals(((HashMap) jobList.get(j)).get("jobName"))) {
                                ((ArrayList) ((HashMap) jobList.get(i)).get("nodes")).add(((ArrayList) ((HashMap) jobList.get(j)).get("nodes")).get(0));
                                jobList.set(j, emptyMap);
                            }
                        }
                    }
                }
            }
            jobList = (List) jobList.stream().filter(item -> ((HashMap) item).size() != 0).collect(Collectors.toList());
        }

        return JsonUtil.writeValueAsString(jobList);
    }

}