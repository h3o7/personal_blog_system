package blogsystem.service.impl;

import blogsystem.dto.ArticleDTO;
import blogsystem.dto.ArticleMessage;
import blogsystem.dto.NewArticleDTO;
import blogsystem.entity.ArticleTag;
import blogsystem.entity.ArticleTagList;
import blogsystem.entity.ArticleType;
import blogsystem.mapper.*;
import blogsystem.utils.CurrentHolder;
import blogsystem.vo.ArticleVO;
import blogsystem.vo.PageResult;
import blogsystem.entity.Article;
import blogsystem.service.IArticleService;
import blogsystem.vo.Result;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static blogsystem.utils.RedisConstants.*;
import static blogsystem.utils.SystemConstants.ARTICLE_EXCHANGE_NAME;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserMapper userMapper;
    private final ArticleTypeMapper articleTypeMapper;
    private final ArticleTagListMapper articleTagListMapper;
    private final ArticleTagMapper articleTagMapper;

    // 管理员获取列表的方法
    @Override
    public PageResult getArticleList(ArticleDTO articleDto) {
        // 设置分页参数
        Page<ArticleDTO> page = new Page<>(articleDto.getPageNumber(), articleDto.getPageSize());
        // 调用Mapper方法获取分页数据
        Page<ArticleDTO> articlePage = baseMapper.getArticleList(page, articleDto);
        // 封装结果
        return new PageResult(articlePage.getTotal(), articlePage.getRecords());
    }

    @Override
    public Result likeArticle(String articleId) {
        // 获取当前用户ID
        String userId = CurrentHolder.getCurrentId();
        String likeKey = USER_LIKE_BLOG_PREFIX + userId;
        // 检查文章是否已被点赞
        Double member = stringRedisTemplate.opsForZSet().score(likeKey, articleId);
        ArticleMessage articleMessage = new ArticleMessage(userId, articleId);
        if (member != null) {
            String routingKey = "unlike";
            rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE_NAME, routingKey, articleMessage);
            return Result.success("取消点赞成功");
        }
        String routingKey = "like";
        rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE_NAME, routingKey, articleMessage);
        return Result.success("点赞成功");
    }

    @Override
    public Result collectArticle(String articleId) {

        // 获取当前用户ID
        String userId = CurrentHolder.getCurrentId();
        String collectKey = USER_COLLECT_BLOG_PREFIX + userId;

        // 检查文章是否已被点赞
        Double member = stringRedisTemplate.opsForZSet().score(collectKey, articleId);
        ArticleMessage articleMessage = new ArticleMessage(userId, articleId);
        if (member != null) {
            String routingKey = "unCollect";
            rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE_NAME, routingKey, articleMessage);
            return Result.success("取消收藏成功");
        }
        String routingKey = "collect";
        rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE_NAME, routingKey, articleMessage);
        return Result.success("收藏成功");


    }

    @Override
    public Result getArticleDetail(String articleId) {
        UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("article_id", articleId)
                .setSql("article_look_number = article_look_number + 1");
        baseMapper.update(null, updateWrapper);
        String userId = CurrentHolder.getCurrentId();
        String likeKey = USER_LIKE_BLOG_PREFIX + userId;
        String collectKey = USER_COLLECT_BLOG_PREFIX + userId;
        Article article = baseMapper.selectById(articleId);
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        articleVO.setUserName(userMapper.selectById(article.getUserId()).getUserName());
        articleVO.setUserAvatar(userMapper.selectById(article.getUserId()).getUserAvatar());
        articleVO.setArticleType(articleTypeMapper.selectById(article.getArticleTypeId()).getArticleTypeName());
        List<String> tags = articleTagListMapper.selectTagsById(article.getArticleId());
        articleVO.setArticleTags(tags);
        Double scoreLike = stringRedisTemplate.opsForZSet().score(likeKey, article.getArticleId());
        if (scoreLike != null) {
            articleVO.setLiked(true);
        }
        Double scoreCollect = stringRedisTemplate.opsForZSet().score(collectKey, article.getArticleId());
        if (scoreCollect != null) {
            articleVO.setCollected(true);
        }
        return Result.success(articleVO);
    }

    @Override
    @Transactional
    public Result addArticle(NewArticleDTO newArticleDTO) {

        String userId = CurrentHolder.getCurrentId();
        Article article = new Article();

        // 保存文章相关信息
        article.setArticleId(IdUtil.simpleUUID());
        String articleTypeId = articleTypeMapper.selectOne(new LambdaQueryWrapper<ArticleType>()
                        .eq(ArticleType::getArticleTypeName, newArticleDTO.getArticleType()))
                .getArticleTypeId();
        article.setArticleTypeId(articleTypeId);
        article.setUserId(userId);
        article.setArticleTitle(newArticleDTO.getArticleTitle());
        article.setArticleContent(newArticleDTO.getArticleContent());
        article.setArticleAddTime(LocalDateTime.now());
        baseMapper.insert(article);

        // 保存文章标签

        List<String> articleTags = newArticleDTO.getArticleTags();
        for (String articleTag : articleTags) {
            String articleTagId = articleTagMapper.selectOne(new LambdaQueryWrapper<ArticleTag>()
                            .eq(ArticleTag::getArticleTagName, articleTag))
                    .getArticleTagId();
            ArticleTagList articleTagList = new ArticleTagList();
            articleTagList.setArticleTagListId(IdUtil.simpleUUID());
            articleTagList.setArticleId(article.getArticleId());
            articleTagList.setArticleTagId(articleTagId);
            articleTagListMapper.insert(articleTagList);
        }
        // 清除Redis中保存的草稿
        String redisTitleKey = BLOG_TEMP_TITLE_PREFIX + userId;
        String redisContentKey = BLOG_TEMP_CONTENT_PREFIX + userId;
        stringRedisTemplate.delete(redisTitleKey);
        stringRedisTemplate.delete(redisContentKey);
        return Result.success("文章发布成功");
    }

    @Override
    public Result saveArticle(NewArticleDTO newArticleDTO) {

        String userId = CurrentHolder.getCurrentId();
        String titleKey = BLOG_TEMP_TITLE_PREFIX + userId;
        String contentKey = BLOG_TEMP_CONTENT_PREFIX + userId;
        // 将文章标题和内容存入Redis
        stringRedisTemplate.opsForValue().set(titleKey, newArticleDTO.getArticleTitle());
        stringRedisTemplate.opsForValue().set(contentKey, newArticleDTO.getArticleContent());
        return Result.success("文章草稿已保存");
    }

    @Override
    public Result getDraft() {
        NewArticleDTO newArticleDTO = new NewArticleDTO();
        String userId = CurrentHolder.getCurrentId();
        String articleTitleKey = BLOG_TEMP_TITLE_PREFIX + userId;
        String articleContentKey = BLOG_TEMP_CONTENT_PREFIX + userId;
        // 从Redis中获取文章标题和内容
        String articleTitle = stringRedisTemplate.opsForValue().get(articleTitleKey);
        String articleContent = stringRedisTemplate.opsForValue().get(articleContentKey);
        newArticleDTO.setArticleTitle(articleTitle);
        newArticleDTO.setArticleContent(articleContent);
        return Result.success(newArticleDTO);
    }

    // 首页展示的Blog内容
    @Override
    public Result getArticleList(LocalDateTime addTime, Integer count) {
        List<Article> list = baseMapper.selectList(new LambdaQueryWrapper<Article>().
                lt(Article::getArticleAddTime, addTime)
                .orderByDesc(Article::getArticleAddTime)
                .last("LIMIT " + count));
        if (list.isEmpty() || list == null) {
            return Result.error("没有找到相关的文章");
        }
        try {
            // 装饰文章信息
            List<ArticleVO> articleVOList = decorateArticles(list);
            // 如果装饰过程中出现问题导致列表为空
            if (articleVOList == null || articleVOList.isEmpty()) {
                return Result.error("获取文章详情失败");
            }
            return Result.success(articleVOList);
        } catch (Exception e) {
            log.error("文章装饰过程出错", e);
            return Result.error("处理文章数据时发生错误");
        }
    }

    @Override
    public Result getHotArticleList(Integer limit) {
        List<Article> list = baseMapper.selectList(new LambdaQueryWrapper<Article>()
                .orderByDesc(Article::getArticleGoodNumber)
                .last("LIMIT " + limit));
        if (list.isEmpty() || list == null) {
            return Result.error("没有找到相关的文章");
        }
        try {
            // 装饰文章信息
            List<ArticleVO> articleVOList = decorateArticles(list);
            // 如果装饰过程中出现问题导致列表为空
            if (articleVOList == null || articleVOList.isEmpty()) {
                return Result.error("获取文章详情失败");
            }
            return Result.success(articleVOList);
        } catch (Exception e) {
            log.error("文章装饰过程出错", e);
            return Result.error("处理文章数据时发生错误");
        }
    }

    @Override
    public Result getArticleByType(String typeId, Integer limit) {
        List<Article> list = baseMapper.selectList(new LambdaQueryWrapper<Article>()
                .eq(Article::getArticleTypeId, typeId)
                .orderByDesc(Article::getArticleAddTime)
                .last("LIMIT " + limit));
        if (list.isEmpty() || list == null) {
            return Result.error("没有找到相关的文章");
        }
        try {
            // 装饰文章信息
            List<ArticleVO> articleVOList = decorateArticles(list);
            // 如果装饰过程中出现问题导致列表为空
            if (articleVOList == null || articleVOList.isEmpty()) {
                return Result.error("获取文章详情失败");
            }
            return Result.success(articleVOList);
        } catch (Exception e) {
            log.error("文章装饰过程出错", e);
            return Result.error("处理文章数据时发生错误");
        }
    }

    @Override
    public Result searchArticle(String keyword, Integer limit) {
        List<Article> list = baseMapper.selectList(new LambdaQueryWrapper<Article>()
                .like(Article::getArticleTitle, keyword)
                .orderByDesc(Article::getArticleAddTime)
                .last("LIMIT " + limit));
        if (list.isEmpty() || list == null) {
            return Result.error("没有找到相关的文章");
        }
        try {
            // 装饰文章信息
            List<ArticleVO> articleVOList = decorateArticles(list);
            // 如果装饰过程中出现问题导致列表为空
            if (articleVOList == null || articleVOList.isEmpty()) {
                return Result.error("获取文章详情失败");
            }
            return Result.success(articleVOList);
        } catch (Exception e) {
            log.error("文章装饰过程出错", e);
            return Result.error("处理文章数据时发生错误");
        }
    }


    // 封装对文章进行修饰的共同逻辑
    private List<ArticleVO> decorateArticles(List<Article> list) {
        String userId = CurrentHolder.getCurrentId();
        String likeKey = USER_LIKE_BLOG_PREFIX + userId;
        String collectKey = USER_COLLECT_BLOG_PREFIX + userId;
        List<ArticleVO> articleVOList = new ArrayList<>();
        try {
            for (Article article : list) {
                ArticleVO articleVO = new ArticleVO();
                BeanUtils.copyProperties(article, articleVO);
                articleVO.setArticleContent(article.getArticleContent().substring(0, Math.min(article.getArticleContent().length(), 37))); // 截取前100个字符
                articleVO.setUserName(userMapper.selectById(article.getUserId()).getUserName());
                articleVO.setUserAvatar(userMapper.selectById(article.getUserId()).getUserAvatar());
                articleVO.setArticleType(articleTypeMapper.selectById(article.getArticleTypeId()).getArticleTypeName());
                List<String> tags = articleTagListMapper.selectTagsById(article.getArticleId());
                articleVO.setArticleTags(tags);
                // 获取用户点赞状态
                Double scoreLike = stringRedisTemplate.opsForZSet().score(likeKey, article.getArticleId());
                if (scoreLike != null) {
                    articleVO.setLiked(true);
                }
                Double scoreCollect = stringRedisTemplate.opsForZSet().score(collectKey, article.getArticleId());
                if (scoreCollect != null) {
                    articleVO.setCollected(true);
                }
                articleVOList.add(articleVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return articleVOList;
    }
}
