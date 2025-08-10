package blogsystem.service;

import blogsystem.entity.ArticleTag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
public interface IArticleTagService extends IService<ArticleTag> {

    boolean deleteById(String articleTagId);
}
