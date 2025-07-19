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
@TableName("ad_type")
public class AdType implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 广告类型id
     */
    @TableId("ad_type_id")
    private String adTypeId;

    /**
     * 广告类型名称
     */
    private String adTypeTitle;

    /**
     * 广告标识（首页顶部广告，轮廓图广告，文章详情广告）
     */
    private String adTypeTag;

    /**
     * 广告类型排序（越小越靠前）
     */
    private Integer adTypeSort;

    /**
     * 广告类型添加时间
     */
    private LocalDateTime adTypeAddTime;
}
