package blogsystem.service;

import blogsystem.entity.Comment;
import blogsystem.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
public interface ICommentService extends IService<Comment> {

    Result getArticleCommentList(String articleId, Integer limit);
    Result getReplyCommentList(String commentId, Integer limit);

    Result addArticleComment(Comment comment);
    Result addReplyComment(Comment comment);

    Result likeComment(String commentId);

    Result deleteComment(String commentId);
}
