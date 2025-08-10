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
 * @since 2025-08-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("article_type")
public class ArticleType implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文章类型id
     */
    @TableId("article_type_id")
    private String articleTypeId;

    /**
     * 文章类型名
     */
    private String articleTypeName;

    /**
     * 排序（序号越小越靠前）
     */
    private Integer articleTypeSort;

    /**
     * 文章类型添加时间
     */
    private LocalDateTime articleTypeAddTime;
}
