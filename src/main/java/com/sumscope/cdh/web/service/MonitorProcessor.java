package com.sumscope.cdh.web.service;

/**
 * Created by wenshuai.li on 2016/11/23.
 */
public interface MonitorProcessor<T> {
    void process(T message);
}
