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
public class Article implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @TableId("article_id")
    private String articleId;

    private String articleTypeId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章添加时间
     */
    private LocalDateTime articleAddTime;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 点赞次数
     */
    private Integer articleGoodNumber;

    /**
     * 阅读次数
     */
    private Integer articleLookNumber;

    /**
     * 收藏次数
     */
    private Integer articleCollectionNumber;
}
