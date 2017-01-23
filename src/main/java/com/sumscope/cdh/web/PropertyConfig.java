package com.sumscope.cdh.web;

import com.sumscope.cdh.sumscopezk4j.support.spring.ZooKeeperPropertyPlaceholderConfigurer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Created by wenshuai.li on 2016/11/9.
 */
@Component
@Configuration
public class PropertyConfig {
    //本地多个配置文件路径
    private ClassPathResource[] configFiles = null;
    //zk上多个配置文件路径
    private String[] configPaths = null;
    @Bean
    public PropertyPlaceholderConfigurer zooKeeperPropertyPlaceholderConfigurer(){
        preparePropertiesByEnv();
        ZooKeeperPropertyPlaceholderConfigurer zc = new ZooKeeperPropertyPlaceholderConfigurer();
        zc.setConnectString(System.getProperty("spring.zookeeper.connect"));
        zc.setPath(configPaths);
        zc.setIgnoreResourceNotFound(true);
        zc.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");
        zc.setLocations(configFiles);
        //zc.setDownloadFileString(System.getProperty("spring.zookeeper.downloadFileString"));
        return zc;
    }

    private void preparePropertiesByEnv() {
        initConfigFiles();//本地多个配置文件
        initConfigPaths();//zk上多个配置文件path

    }

    private void initConfigFiles(){
        String currentConfigs = System.getProperty("spring.config.name");
        String[] strs = StringUtils.split(currentConfigs,",");
        configFiles = new ClassPathResource[strs.length];
        for(int i=0;i<strs.length;i++){
            configFiles[i] = new ClassPathResource(strs[i]);
        }
    }

    private void initConfigPaths(){
        try{
            String currentConfigs = System.getProperty("web.path");
            configPaths = StringUtils.split(currentConfigs,",");
        }catch (Exception e){
        }

    }
}
