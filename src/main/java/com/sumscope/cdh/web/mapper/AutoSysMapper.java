
package com.sumscope.cdh.web.mapper;
import com.sumscope.cdh.monitor.model.autosys.Message;
import com.sumscope.cdh.web.domain.Autosys;
import com.sumscope.cdh.web.domain.AutosysSqlModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Roidder on 2016/10/10.
 */

public class AutoSysMapper {

    public List<Message> queryJobInfo(Map map) {
        return dataServiceSessionTemplate.selectList("jobInfo", map);
    }

    public List<Autosys> queryTriggerInfo(Map map) {
        return dataServiceSessionTemplate.selectList("triggerInfo", map);
    }

    public void insertIntoTriggerLog(AutosysSqlModel message){
        dataServiceSessionTemplate.insert("insertIntoTriggerLog", message);
    }
    @Autowired
    private SqlSessionTemplate dataServiceSessionTemplate;

    public SqlSessionTemplate getDataServiceSessionTemplate() {
        return dataServiceSessionTemplate;
    }

    public void setDataServiceSessionTemplate(SqlSessionTemplate dataServiceSessionTemplate) {
        this.dataServiceSessionTemplate = dataServiceSessionTemplate;
    }

    private String databaseName;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
