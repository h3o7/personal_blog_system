package blogsystem.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户粉丝表
 * </p>
 *
 * @author h3o7
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_fan")
public class UserFan implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("user_fan_id")
    private String userFanId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 粉丝id
     */
    private String fanId;

    /**
     * 关注时间
     */
    private LocalDateTime createTime;
}
