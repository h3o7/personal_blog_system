package blogsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {
    private String commentId;
    private String articleId;

    private String userId;

    private String commentContent;
    private LocalDateTime commentTime;
    private Integer commentGoodNumber;

    private String userName;
    private String userAvatar;
    private String repliedName;
    private boolean liked;

}
