package blogsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTypeDTO {
    private String articleTypeId;
    private Integer articleTypeSort; // 文章类型排序
    private String articleTypeName; // 文章类型名称
    private Integer articleNum; // 文章数量
    private LocalDateTime articleTypeAddTime; // 文章类型添加时间
}
