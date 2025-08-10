package blogsystem.service.impl;

import blogsystem.entity.UserCollectArticle;
import blogsystem.mapper.UserCollectArticleMapper;
import blogsystem.service.IUserCollectArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户收藏文章关系表 服务实现类
 * </p>
 *
 * @author h3o7
 * @since 2025-08-09
 */
@Service
public class UserCollectArticleServiceImpl extends ServiceImpl<UserCollectArticleMapper, UserCollectArticle> implements IUserCollectArticleService {

}
