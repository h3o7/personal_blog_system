package blogsystem.service;

import blogsystem.dto.ArticleDTO;
import blogsystem.dto.NewArticleDTO;
import blogsystem.vo.PageResult;
import blogsystem.entity.Article;
import blogsystem.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
public interface IArticleService extends IService<Article> {
    PageResult getArticleList(ArticleDTO articleDto);

    Result getArticleList(LocalDateTime createTime, Integer limit);

    Result likeArticle(String articleId);

    Result collectArticle(String articleId);

    Result getArticleDetail(String articleId);

    Result addArticle(NewArticleDTO newArticleDTO);

    Result saveArticle(NewArticleDTO newArticleDTO);

    Result getDraft();

    Result getHotArticleList(Integer limit);

    Result searchArticle(String keyword, Integer limit);

    Result getArticleByType(String typeId, Integer limit);
}
