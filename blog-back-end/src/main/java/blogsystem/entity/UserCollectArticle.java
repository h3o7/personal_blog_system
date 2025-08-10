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
 * 用户收藏文章关系表
 * </p>
 *
 * @author h3o7
 * @since 2025-08-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_collect_article")
public class UserCollectArticle implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户收藏文章id
     */
    @TableId("user_collect_article_id")
    private String userCollectArticleId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 用户收藏文章时间
     */
    private LocalDateTime addTime;
}
