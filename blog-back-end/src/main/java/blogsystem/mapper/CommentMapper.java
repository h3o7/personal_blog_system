package blogsystem.mapper;

import blogsystem.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectHotCommentsByArticleId(String articleId, Integer limit);

    List<Comment> selectHotCommentsByCommentId(String commentId, Integer limit);

}
