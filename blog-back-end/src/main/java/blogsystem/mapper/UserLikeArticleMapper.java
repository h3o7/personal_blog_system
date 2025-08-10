package blogsystem.mapper;

import blogsystem.entity.UserLikeArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLikeArticleMapper extends BaseMapper<UserLikeArticle> {
}
