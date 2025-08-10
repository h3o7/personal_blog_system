package blogsystem.listener;

import blogsystem.dto.ArticleMessage;
import blogsystem.entity.Article;
import blogsystem.entity.UserCollectArticle;
import blogsystem.entity.UserLikeArticle;
import blogsystem.mapper.ArticleMapper;
import blogsystem.mapper.UserCollectArticleMapper;
import blogsystem.mapper.UserLikeArticleMapper;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static blogsystem.utils.RedisConstants.USER_COLLECT_BLOG_PREFIX;
import static blogsystem.utils.RedisConstants.USER_LIKE_BLOG_PREFIX;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleListener {
    private final StringRedisTemplate stringRedisTemplate;
    private final ArticleMapper articleMapper;
    private final UserLikeArticleMapper userLikeArticleMapper;
    private final UserCollectArticleMapper userCollectArticleMapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "article_like_queue", durable = "true"),
            exchange = @Exchange(value = "article.direct", type = ExchangeTypes.DIRECT),
            key = "like"
    ))
    public void likeArticle(ArticleMessage articleMessage) {
        Long start = System.currentTimeMillis();
        String userId = articleMessage.getUserId();
        String articleId = articleMessage.getArticleId();
        log.info("收到点赞消息，文章ID: {},{}", userId,articleId);

        String likeKey = USER_LIKE_BLOG_PREFIX + userId;
        // 加入缓存
        stringRedisTemplate.opsForZSet().add(likeKey, articleId, System.currentTimeMillis());
        // 数据持久化 - 数量
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getArticleId, articleId)
                .setSql("article_good_number = article_good_number + 1");
        articleMapper.update(null, updateWrapper);
        // 数据持久化 - 记录
        UserLikeArticle userLikeArticle = new UserLikeArticle(IdUtil.simpleUUID(),userId, articleId,LocalDateTime.now());
        userLikeArticleMapper.insert(userLikeArticle);
        Long end = System.currentTimeMillis();
        System.err.println("取消点赞操作耗时: " + (end - start) + "毫秒");

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "article_unlike_queue", durable = "true"),
            exchange = @Exchange(value = "article.direct", type = ExchangeTypes.DIRECT),
            key = "unlike"
    ))
    public void unlikeArticle(ArticleMessage articleMessage) {
        Long start = System.currentTimeMillis();
        String userId = articleMessage.getUserId();
        String articleId = articleMessage.getArticleId();
        log.info("收到取消点赞消息，文章ID: {},{}", userId,articleId);

        String likeKey = USER_LIKE_BLOG_PREFIX + userId;
        // 加入缓存
        stringRedisTemplate.opsForZSet().remove(likeKey, articleId);
        // 数据持久化 - 数量
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getArticleId, articleId)
                .setSql("article_good_number = article_good_number - 1");
        articleMapper.update(null, updateWrapper);
        // 数据持久化 - 记录
        LambdaQueryWrapper<UserLikeArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLikeArticle::getUserId, userId)
                .eq(UserLikeArticle::getArticleId, articleId);
        userLikeArticleMapper.delete(queryWrapper);
        Long end = System.currentTimeMillis();
        System.err.println("取消点赞操作耗时: " + (end - start) + "毫秒");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "article_collect_queue", durable = "true"),
            exchange = @Exchange(value = "article.direct", type = ExchangeTypes.DIRECT),
            key = "collect"
    ))
    public void collectArticle(ArticleMessage articleMessage) {
        String userId = articleMessage.getUserId();
        String articleId = articleMessage.getArticleId();
        String collectKey = USER_COLLECT_BLOG_PREFIX + userId;
        // 加入缓存
        stringRedisTemplate.opsForZSet().add(collectKey, articleId, System.currentTimeMillis());
        // 数据持久化 - 数量
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getArticleId, articleId)
                .setSql("article_collection_number = article_collection_number + 1");
        articleMapper.update(null, updateWrapper);
        // 数据持久化 - 记录
        UserCollectArticle userCollectArticle = new UserCollectArticle(IdUtil.simpleUUID(), userId, articleId, LocalDateTime.now());
        userCollectArticleMapper.insert(userCollectArticle);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "article_unCollect_queue", durable = "true"),
            exchange = @Exchange(value = "article.direct", type = ExchangeTypes.DIRECT),
            key = "unCollect"
    ))
    public void unCollectArticle(ArticleMessage articleMessage) {
        String userId = articleMessage.getUserId();
        String articleId = articleMessage.getArticleId();
        String collectKey = USER_COLLECT_BLOG_PREFIX + userId;
        // 加入缓存
        stringRedisTemplate.opsForZSet().remove(collectKey, articleId);
        // 数据持久化 - 数量
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getArticleId, articleId)
                .setSql("article_collection_number = article_collection_number - 1");
        articleMapper.update(null, updateWrapper);
        // 数据持久化 - 记录
        LambdaQueryWrapper<UserCollectArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCollectArticle::getUserId, userId)
                .eq(UserCollectArticle::getArticleId, articleId);
        userCollectArticleMapper.delete(queryWrapper);
    }



}
