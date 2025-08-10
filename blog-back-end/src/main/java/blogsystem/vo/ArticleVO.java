package blogsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVO {
    private String articleId;
    private String userId;
    private String articleTitle;
    private LocalDateTime articleAddTime;
    private String articleContent;
    private Integer articleGoodNumber;
    private Integer articleLookNumber;
    private Integer articleCollectionNumber;

    private String userName;
    private String userAvatar;
    private String articleType;
    private List<String> articleTags;
    private boolean liked = false;
    private boolean collected = false;
}
