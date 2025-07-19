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
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ad implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 广告id
     */
    @TableId("ad_id")
    private String adId;

    /**
     * 广告类型
     */
    private String adTypeId;

    /**
     * 广告标题
     */
    private String adTitle;

    /**
     * 广告图片的url地址
     */
    private String adImgUrl;

    /**
     * 广告链接地址
     */
    private String adLinkUrl;

    /**
     * 广告排序（越小越靠前）
     */
    private Integer adSort;

    /**
     * 广告开始时间
     */
    private LocalDateTime adBeginTime;

    /**
     * 广告结束时间
     */
    private LocalDateTime adEndTime;

    /**
     * 添加广告时间
     */
    private LocalDateTime adAddTime;
}
