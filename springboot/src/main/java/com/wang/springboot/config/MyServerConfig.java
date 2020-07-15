package com.wang.springboot.config;

import com.wang.springboot.filter.MyFilter;
import com.wang.springboot.listener.MyListener;
import com.wang.springboot.servelet.MyServlet;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @ Configuration：指明当前类是一个配置类；就是来替代之前的Spring配置文件
 *
 * 在配置文件中用<bean><bean/>标签添加组件
 *
 */
@Configuration
public class MyServerConfig {

    // 注册 servelet 三大组件
    @Bean
    public ServletRegistrationBean myServlet() {
        ServletRegistrationBean servletBean = new ServletRegistrationBean();
        servletBean.setServlet(new MyServlet());
        servletBean.setUrlMappings(Arrays.asList("/myServlet"));
        return servletBean;
    }

    @Bean
    public FilterRegistrationBean myFilter() {
        FilterRegistrationBean filterBean = new FilterRegistrationBean<MyFilter>();
        filterBean.setFilter(new MyFilter());
        filterBean.setUrlPatterns(Arrays.asList("/hello", "/myServlet"));
        return filterBean;
    }

    @Bean
    public ServletListenerRegistrationBean myListener() {
        ServletListenerRegistrationBean<MyListener> listenerBean = new ServletListenerRegistrationBean<>();
        listenerBean.setListener(new MyListener());
        return listenerBean;
    }


    // 配置嵌入式的 servelet 容器, SpringBoot2.0 以上，使用 WebServerFactoryCustomizer 接口
    // SpringBoot2.0 以下，使用 EmbeddedServletContainerCustomizer
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>(){
            // 定制嵌入式的Servlet容器相关的规则
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                factory.setPort(8080);
            }
        };
    }
}
