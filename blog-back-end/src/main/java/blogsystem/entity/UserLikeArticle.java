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
 * 用户喜欢文章关系
 * </p>
 *
 * @author h3o7
 * @since 2025-08-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_like_article")
public class UserLikeArticle implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户文章表id
     */
    @TableId("user_like_article_id")
    private String userLikeArticleId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 用户喜欢文章的时间
     */
    private LocalDateTime addTime;
}
