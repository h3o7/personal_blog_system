package blogsystem.mapper;

import blogsystem.entity.ArticleTagList;
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
public interface ArticleTagListMapper extends BaseMapper<ArticleTagList> {

    List<String> selectTagsById(String articleId);

}
