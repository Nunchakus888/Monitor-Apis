
package com.sumscope.cdh.web.mapper;

import com.sumscope.cdh.web.domain.UserInfo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by Roidder on 2016/10/10.
 */

public class UserMapper {

    @Autowired
    private SqlSessionTemplate dataServiceSessionTemplate;

    //register
    public int register(Map map){
        return dataServiceSessionTemplate.insert("register", map);
    }

    //login
    public UserInfo login(Map map){
        return dataServiceSessionTemplate.selectOne("login", map);
    }

    //update password
    public int updatePassword(Map map){
        return dataServiceSessionTemplate.update("updatePassword", map);
    }

    //forgot password
    public UserInfo findPassword(Map map){
        return dataServiceSessionTemplate.selectOne("findPassword", map);
    }


    public int createTable(String tableName){
        return dataServiceSessionTemplate.update("createNewTable", tableName);
    }

    public SqlSessionTemplate getDataServiceSessionTemplate() {
        return dataServiceSessionTemplate;
    }

    public void setDataServiceSessionTemplate(SqlSessionTemplate dataServiceSessionTemplate) {
        this.dataServiceSessionTemplate = dataServiceSessionTemplate;
    }
}
