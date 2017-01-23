package com.sumscope.cdh.rabbitmq;


import com.sumscope.cdh.monitor.model.Base;
import com.sumscope.cdh.monitor.model.ExceptionInfo;
import com.sumscope.cdh.web.domain.MonitorObj;
import com.sumscope.cdh.web.util.JsonUtil;
import com.sumscope.cdh.web.util.TaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Created by wenshuai.li on 2016/11/23.
 */
@Component
public class ExceptionCallback extends BaseMQCallBack {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionCallback.class);

    List<String> serviceKeyList = new ArrayList<>();

    private AtomicLong lastMailTime = null;

    @Autowired
    private com.sumscope.cdh.web.util.EmailUtil emailUtil;

    public ExceptionCallback(){
        serviceKeyList.add("fxspot");
        serviceKeyList.add("fxderivative");
    }
    @Override
    public boolean processString(String s) {
        try{
            ExceptionInfo exceptionInfo = JsonUtil.readValueNoException(s,ExceptionInfo.class);
            logger.info("ExceptionCallback:" + s);
            TaskExecutor.exceptionQueue.offer(exceptionInfo);

            MonitorObj monitorObj = new MonitorObj(Base.MonitorType.EXCEPTION.value());
            monitorObj.setData(exceptionInfo);
            send(JsonUtil.writeValueAsString(monitorObj));

            doEmail(exceptionInfo);
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean processBytes(byte[] bytes) {
        return processString(new String(bytes));
    }

    private void doEmail(ExceptionInfo exceptionInfo){
        long currentTime = System.currentTimeMillis();
        long lasts = (lastMailTime == null?0L:currentTime - lastMailTime.get());
        if(lastMailTime == null || (lasts / 1000) > 60){//1分钟
            email(exceptionInfo);
            lastMailTime = new AtomicLong(currentTime);
        }
    }
    private void email(ExceptionInfo exceptionInfo){
        if(exceptionInfo != null && exceptionInfo.getServiceKey() != null
                && serviceKeyList.contains(exceptionInfo.getServiceKey())){
            try{
                Map<String,String> map = new HashMap<>();
                map.put("errorCode",exceptionInfo.getErrorCode());
                map.put("errorMessage",exceptionInfo.getErrorMessage());
                map.put("errorStack",exceptionInfo.getErrorStack());
                map.put("serviceKey",exceptionInfo.getServiceKey());
                map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(exceptionInfo.getTime())) );
                emailUtil.sendNoticeEmail(JsonUtil.writeValueAsString(map));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
