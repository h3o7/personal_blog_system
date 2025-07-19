package blogsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private String articleId;
    private String articleTypeName;
    private String userName;
    private String articleTitle;
    private LocalDateTime articleAddTime;
    private String articleContent;
    private Integer articleGoodNumber;
    private Integer articleLookNumber;
    private Integer articleCollectionNumber;
    private Integer pageNumber = 1; // 当前页码
    private Integer pageSize = 10; // 每页记录数
}
