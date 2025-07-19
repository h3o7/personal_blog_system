package blogsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewArticleDTO {
    private String articleTitle;
    private String articleContent;

    private String articleType;
    private List<String> articleTags;

}
