package com.sumscope.cdh.web; /**
 * Created by Administrator on 2015/10/23.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource("classpath:bean-config.xml")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan("com.sumscope.cdh")
public class ServletInitializer extends SpringBootServletInitializer {


    public static void main(String[] args) throws Exception {
        //System.setProperty("spring.config.name","application.properties,application-profile.properties");
        //System.setProperty("spring.zookeeper.connect","172.16.87.2:2181,172.16.66.171:2181,172.16.66.172:2181");
        //System.setProperty("web.path","/com/sumscope/mysql/dev,/com/sumscope/rabbitmq/dev");
        //System.setProperty("spring.zookeeper.downloadFileString","http://172.16.73.48:9900/configManage/downloadFile?file=");
        System.out.println("spring.config.name->"+System.getProperty("spring.config.name"));
        System.out.println("spring.zookeeper.connect->"+System.getProperty("spring.zookeeper.connect"));
        //System.out.println("web.path->"+System.getProperty("web.path"));
        System.out.println("spring.zookeeper.downloadFileString->"+System.getProperty("spring.zookeeper.downloadFileString"));
        SpringApplication.run(ServletInitializer.class, args);
    }
}