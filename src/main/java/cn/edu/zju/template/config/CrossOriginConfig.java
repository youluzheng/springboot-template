package cn.edu.zju.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zyl
 * 跨域配置
 *
 */
@Configuration
public class CrossOriginConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 拦截所有请求
        registry.addMapping("/**")
                .allowedHeaders("*")
                // 容许所有来源（解决跨域问题）
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}
