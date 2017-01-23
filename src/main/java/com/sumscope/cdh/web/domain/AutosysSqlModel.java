package com.sumscope.cdh.web.domain;

import com.sumscope.cdh.monitor.model.autosys.Message;
import com.sumscope.cdh.web.util.DateUtil;

/**
 * Created by Roidder on 2016/12/9.
 */
public class AutosysSqlModel extends Message {
    private String sqlTime;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    private String database;

    public AutosysSqlModel(){

    }

    public AutosysSqlModel(Message message) {
        this.type = message.getType();
        this.uuid = message.getUuid();
        this.updateTime = message.getUpdateTime();
        this.triggerGroup = message.getTriggerGroup();
        this.triggerName = message.getTriggerName();
        this.schedName = message.getSchedName();
        this.detail = message.getDetail();
        sqlTime = DateUtil.getDatetime(updateTime);
    }

    public void setUpdateTime(long updateTime) {
        super.setUpdateTime(updateTime);
        sqlTime = DateUtil.getDatetime(updateTime);
    }

    public String getSqlTime() {
        return sqlTime;
    }

}
