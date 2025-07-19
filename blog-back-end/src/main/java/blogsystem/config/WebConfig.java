package blogsystem.config;

import blogsystem.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器对象
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**")
//                .excludePathPatterns("/login/**")//设置拦截器拦截的请求路径（ /** 表示拦截所有请求）
                .excludePathPatterns(
                        "/api/auth/login",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/api-docs/**",
                        "/webjars/**",
                        "/captcha/**",
                        "/login/**"


                );
    }
}
