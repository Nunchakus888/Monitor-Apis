package com.sumscope.cdh.web.service;

import com.sumscope.cdh.monitor.model.autosys.Message;
import com.sumscope.cdh.web.domain.Autosys;
import com.sumscope.cdh.web.domain.AutosysSqlModel;
import com.sumscope.cdh.web.mapper.AutoSysMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wenshuai.li on 2016/11/23.
 */
@Service("AutoSysService")
public class AutoSysService {

    @Autowired
    AutoSysMapper autoSysMapper;

    public List<Message> queryJobInfo(Map map) {
        map.put("database", autoSysMapper.getDatabaseName());
        return autoSysMapper.queryJobInfo(map);
    }

    public List<Autosys> queryTriggerInfo(Map map) {
        return autoSysMapper.queryTriggerInfo(map);
    }

    public void insertIntoTriggerLog(AutosysSqlModel message) {
        message.setDatabase(autoSysMapper.getDatabaseName());
        autoSysMapper.insertIntoTriggerLog(message);
    }

    public List<Message> queryBusinessInfo(Map map) {
        return (List<Message>) autoSysMapper.queryJobInfo(map);
    }

    /*public int queryTotalSize(Map map) {
        return autoSysMapper.queryTotalSize(map);
    }*/

}
