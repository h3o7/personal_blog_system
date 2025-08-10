package blogsystem.dto;

import lombok.Data;

/**
 * 登录请求DTO
 */
@Data
public class LoginDTO {
    private String id;
    private String password;
    private String captchaId;
    private String captchaCode;
}