package blogsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        // 服务器信息
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("本地开发环境");

        // 安全方案
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("请在此处输入 JWT Token，格式：Bearer {token}");

        // 安全要求
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");

        return new OpenAPI()
                .servers(List.of(localServer))
                .info(new Info()
                        .title("个人博客系统 API")
                        .description("基于 SpringBoot 3.4.5 的个人博客系统接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("h3o7")
                                .email("h3o7@example.com")
                                .url("https://github.com/h3o7"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}