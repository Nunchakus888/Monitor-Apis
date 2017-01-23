package com.sumscope.cdh.rabbitmq;

import com.sumscope.cdh.monitor.model.Base;
import com.sumscope.cdh.monitor.model.BusinessInfo;
import com.sumscope.cdh.web.domain.MonitorObj;
import com.sumscope.cdh.web.util.JsonUtil;
import com.sumscope.cdh.web.util.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by wenshuai.li on 2016/11/23.
 */
@Component
public class BizCallback extends BaseMQCallBack {

    @Override
    public boolean processString(String s) {
        try {
            BusinessInfo businessInfo = JsonUtil.readValueNoException(s,BusinessInfo.class);
            logger.info("BizCallback:" + s);
            TaskExecutor.bizQueue.offer(businessInfo);

            MonitorObj monitorObj = new MonitorObj(Base.MonitorType.BUSINESS.value());

            monitorObj.setData(businessInfo);

            send(JsonUtil.writeValueAsString(monitorObj));
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean processBytes(byte[] bytes) {
        return false;
    }
}
