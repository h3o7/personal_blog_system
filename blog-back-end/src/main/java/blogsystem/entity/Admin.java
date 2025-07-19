package blogsystem.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author h3o7
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 管理员id
     */
    @TableId("admin_id")
    private String adminId;

    /**
     * 管理员姓名
     */
    private String adminName;

    /**
     * 管理员密码
     */
    private String adminPassword;
}
