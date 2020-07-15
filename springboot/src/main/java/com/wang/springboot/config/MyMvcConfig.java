package com.wang.springboot.config;

import com.wang.springboot.component.LoginHandlerIntercetpot;
import com.wang.springboot.component.MyLocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 使用 WebMvcConfigurer 来扩展 SpringMVC 功能
// 使用 @EnableWebMvc 来全面接管 SpringMVC
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 浏览器发送 /success 请求来到 success.html
        registry.addViewController("/success").setViewName("success");
    }

    /**
     * 所有的 WebMvcConfigurer 组件都会一起起作用
     * spring 5 版本之后使用的是 webMvcConfigurer 而不是 webMvcConfigurerAdapter
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        WebMvcConfigurer configurer = new WebMvcConfigurer() {
            // 注册视图控制器
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("login");
                registry.addViewController("/index.html").setViewName("login");
                registry.addViewController("/main.html").setViewName("dashboard");
            }

            // 注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                // SpringBoot已经做好了静态资源(*.css , *.js)映射
                registry.addInterceptor(new LoginHandlerIntercetpot()).addPathPatterns("/**")
                        .excludePathPatterns("/index.html", "/", "/user/login");
            }
        };

        return configurer;
    }

    // 构建国际化组件
    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }
}
