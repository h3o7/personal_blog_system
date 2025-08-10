package blogsystem.service.impl;

import blogsystem.entity.UserLikeArticle;
import blogsystem.mapper.UserLikeArticleMapper;
import blogsystem.service.IUserLikeArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户喜欢文章关系 服务实现类
 * </p>
 *
 * @author h3o7
 * @since 2025-08-09
 */
@Service
public class UserLikeArticleServiceImpl extends ServiceImpl<UserLikeArticleMapper, UserLikeArticle> implements IUserLikeArticleService {

}
