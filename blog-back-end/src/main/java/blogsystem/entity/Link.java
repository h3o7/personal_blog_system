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
public class Link implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 友情链接id
     */
    @TableId("link_id")
    private String linkId;

    /**
     * 友情链接标题
     */
    private String linkTitle;

    /**
     * 友情链接地址
     */
    private String linkUrl;

    /**
     * 友情链接logo
     */
    private String linkLogoUrl;

    /**
     * 排序（序号越小越靠前）
     */
    private Integer linkSort;

    /**
     * 添加友情链接的时间
     */
    private LocalDateTime linkAddTime;
}
