package blogsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 验证码返回对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaVO {
    /**
     * 验证码唯一标识
     */
    private String captchaKey;

    /**
     * 验证码图片（Base64格式）
     */
    private String captchaImage;

    /**
     * 过期时间（秒）
     */
    private Integer expireTime;

    /**
     * 生成时间
     */
    private LocalDateTime createTime;
}