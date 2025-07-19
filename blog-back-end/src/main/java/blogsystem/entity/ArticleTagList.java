package blogsystem.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("article_tag_list")
public class ArticleTagList implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文章对应标签id
     */
    @TableId("article_tag_list_id")
    private String articleTagListId;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 文章标签id
     */
    private String articleTagId;
}
