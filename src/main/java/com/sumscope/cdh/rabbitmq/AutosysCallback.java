package com.sumscope.cdh.rabbitmq;

import com.sumscope.cdh.web.service.AutoSysService;
import com.sumscope.cdh.web.domain.AutosysSqlModel;
import com.sumscope.cdh.web.util.JsonUtil;
import com.sumscope.cdh.web.util.TaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wenshuai.li on 2016/11/23.
 */
@Component
public class AutosysCallback extends BaseMQCallBack {
    @Override
    public boolean processString(String s) {
        process(s.getBytes());
        return true;
    }

    @Autowired
    AutoSysService autoSysService;

    @Override
    public boolean processBytes(byte[] bytes) {
        process(bytes);
        return true;
    }
    
    private void process(byte[] bytes){
        try {
            AutosysSqlModel message = JsonUtil.readValueNoException(new String(bytes), AutosysSqlModel.class);
            logger.info("AutosysCallback:" + new String(bytes));
            TaskExecutor.autosysQueue.offer(message);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
