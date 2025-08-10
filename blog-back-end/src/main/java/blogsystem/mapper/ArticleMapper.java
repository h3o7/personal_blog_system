package blogsystem.mapper;

import blogsystem.dto.ArticleDTO;
import blogsystem.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    Page<ArticleDTO> getArticleList(Page<ArticleDTO> page, ArticleDTO articleDto);

    List<Article> selectByIdsWithOrder(@Param("ids") Collection<String> blogIds);
}
