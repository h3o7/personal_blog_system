package blogsystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {
    private int width = 100; // 验证码图片宽度
    private int height = 40; // 验证码图片高度
    private int length = 4; // 验证码字符长度
    private int expireTime = 60; // 验证码过期时间，单位为秒
}
