package com.sumscope.cdh.web.service;

import com.sumscope.cdh.web.domain.UserInfo;
import com.sumscope.cdh.web.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by Roidder on 2016/10/10.
 */
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //register
    public int register(Map map){
        return userMapper.register(map);
    }

    //login
    public UserInfo login(Map map){
        return userMapper.login(map);
    }

    //update password
    public int updatePassword(Map map){
        return userMapper.updatePassword(map);
    }

    //find password
    public UserInfo findPassword(Map map){
        return userMapper.findPassword(map);
    }

    public int createTable(String tableName){
        int result = userMapper.createTable(tableName);
        return result;
    }

    public UserMapper getUserMapper() {
        return userMapper;
    }

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
