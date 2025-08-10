package blogsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 登录返回VO
 */
@Data
//@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {
    private String id;
    private String name;
    private String token;
    private LocalDateTime loginTime;
}