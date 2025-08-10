package blogsystem.service.impl;

import blogsystem.entity.ArticleTag;
import blogsystem.entity.ArticleTagList;
import blogsystem.mapper.ArticleTagListMapper;
import blogsystem.mapper.ArticleTagMapper;
import blogsystem.service.IArticleTagService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements IArticleTagService {
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ArticleTagListMapper articleTagListMapper;

    @Override
    public boolean deleteById(String articleTagId) {
        // 检查标签是否在使用（即是否存在于article_tag_list表中）
        // 如果存在，则不能删除
        QueryWrapper<ArticleTagList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_tag_id", articleTagId);
        long count = articleTagListMapper.selectCount(queryWrapper);
        if(count > 0){
            return false; // 标签正在被使用，不能删除
        }else {
            // 如果没有被使用，则可以删除
            articleTagMapper.deleteById(articleTagId);
            return true; // 删除成功
        }



    }
}
