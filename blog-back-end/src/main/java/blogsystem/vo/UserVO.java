package blogsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private String userId;
    private String userAvatar;
    private String userName;
    private boolean following = false;
    private Integer userFans;
    private Integer userFollow;   // 关注数
}
