package com.sumscope.cdh.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by wenshuai.li on 2016/11/23.
 */
@Service("AutosysProcessor")
public class AutosysProcessor<AutosysSqlModel> implements MonitorProcessor<AutosysSqlModel> {
    @Autowired
    AutoSysService autoSysService;

    @Override
    public void process(AutosysSqlModel message) {
        try{
            autoSysService.insertIntoTriggerLog((com.sumscope.cdh.web.domain.AutosysSqlModel) message);
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }

}
