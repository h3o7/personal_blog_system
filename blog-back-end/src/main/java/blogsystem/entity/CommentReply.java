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
 * @since 2025-08-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("comment_reply")
public class CommentReply implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评论回复id
     */
    @TableId("comment_reply_id")
    private String commentReplyId;

    /**
     * 一级评论
     */
    private String firstCommentId;

    /**
     * 二级评论
     */
    private String secondCommentId;

    /**
     * 回复评论人的id
     */
    private String replyUserId;
}
