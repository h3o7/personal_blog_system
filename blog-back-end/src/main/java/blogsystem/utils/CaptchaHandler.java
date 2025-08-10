package blogsystem.utils;


import blogsystem.config.CaptchaProperties;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
public class CaptchaHandler {
    private final CaptchaProperties captchaProperties;
    private final StringRedisTemplate stringRedisTemplate;

    // 生成验证码并存储到Redis
    public String generateCaptcha(HttpServletResponse response) throws IOException {
        // 生成验证码逻辑
        LineCaptcha lineCaptcha = new LineCaptcha(
                captchaProperties.getWidth(),
                captchaProperties.getHeight(),
                captchaProperties.getLength(),
                4
        );
        String captchaId = IdUtil.simpleUUID();
        String key = RedisConstants.CAPTCHA_KEY_PREFIX + captchaId;
        stringRedisTemplate.opsForValue().set(key, lineCaptcha.getCode(), captchaProperties.getExpireTime(), TimeUnit.SECONDS);

        // 设置响应头
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // 输出图片到响应流
        lineCaptcha.write(response.getOutputStream());
        return captchaId;
    }

    // 验证验证码
    public boolean validateCaptcha(String captchaId, String code) {
        String key = RedisConstants.CAPTCHA_KEY_PREFIX + captchaId;
        String storedCode = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(storedCode)) {
            return false;
        }
        // 验证码验证后立即删除（一次性使用）
        stringRedisTemplate.delete(key);
        // 忽略大小写比较
        return storedCode.equalsIgnoreCase(code);
    }


}
