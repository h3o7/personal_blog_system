package blogsystem.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author h3o7
 * @since 2025-08-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @TableId("comment_id")
    private String commentId;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 用户id(评论人)
     */
    private String userId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    private LocalDateTime commentTime;

    /**
     * 点赞量
     */
    private Integer commentGoodNumber;
}
