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
 * 
 * </p>
 *
 * @author h3o7
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("article_tag")
public class ArticleTag implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文章标签id
     */
    @TableId("article_tag_id")
    private String articleTagId;

    /**
     * 标签名称
     */
    private String articleTagName;

    /**
     * 添加时间
     */
    private LocalDateTime articleTagAddTime;
}
