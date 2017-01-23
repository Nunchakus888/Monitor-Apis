package com.sumscope.cdh.web;

import com.sumscope.cdh.web.interceptor.SecurityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Created by wenshuai.li on 2015/11/11.
 */

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("**/*.js").addResourceLocations("/");
        registry.addResourceHandler("**/*.json").addResourceLocations("/");
        registry.addResourceHandler("**/favicon.ico").addResourceLocations("/");
        registry.addResourceHandler("**/sockjs.js.map").addResourceLocations("/");
        registry.addResourceHandler("**/*.html").addResourceLocations("/");
        registry.addResourceHandler("**/*.css").addResourceLocations("/");
        registry.addResourceHandler("**/*.jpg").addResourceLocations("/");
        registry.addResourceHandler("**/*.png").addResourceLocations("/");
        registry.addResourceHandler("**/*.woff").addResourceLocations("/");
        registry.addResourceHandler("**/*.woff2").addResourceLocations("/");
        registry.addResourceHandler("**/*.svg").addResourceLocations("/");
        registry.addResourceHandler("**/*.swf").addResourceLocations("/");
        registry.addResourceHandler("**/*.gif").addResourceLocations("/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityInterceptor())
                .addPathPatterns("/configManage/addAndUpload")
                .addPathPatterns("/configManage/updateAndUpload")
                .addPathPatterns("/configManage/createChild")
                .addPathPatterns("/configManage/deleteChild")
                .addPathPatterns("/configManage/updateChild");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*").allowedOrigins("*");
    }
}
