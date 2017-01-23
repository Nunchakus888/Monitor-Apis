
package com.sumscope.cdh.web.util;

import com.sumscope.cdh.monitor.model.BusinessInfo;
import com.sumscope.cdh.monitor.model.ExceptionInfo;
import com.sumscope.cdh.web.domain.AutosysSqlModel;

import java.util.Map;
import java.util.concurrent.*;


/**
 * Created by wenshuai.li on 2015/10/28.
 */

public class TaskExecutor  {
    public static BlockingQueue<BusinessInfo> bizQueue = new LinkedBlockingDeque();
    public static BlockingQueue<ExceptionInfo> exceptionQueue = new LinkedBlockingDeque();
    public static BlockingQueue<AutosysSqlModel> autosysQueue = new LinkedBlockingDeque();
    public static BlockingQueue<Map> connectionsQueue = new LinkedBlockingDeque();
    public static BlockingQueue<Map> throughputQueue = new LinkedBlockingDeque();
    
    public static final ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 7, 1000, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(100));


}

