package blogsystem.utils;

import blogsystem.entity.UserCollectArticle;
import blogsystem.entity.UserLikeArticle;
import blogsystem.mapper.UserCollectArticleMapper;
import blogsystem.mapper.UserLikeArticleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;

import static blogsystem.utils.RedisConstants.USER_COLLECT_BLOG_PREFIX;
import static blogsystem.utils.RedisConstants.USER_LIKE_BLOG_PREFIX;

@Component // 加载为配置类
@RequiredArgsConstructor
@Slf4j
public class LoadInfo {
    private final UserLikeArticleMapper userLikeArticleMapper;
    private final UserCollectArticleMapper userCollectArticleMapper;
    private final StringRedisTemplate stringRedisTemplate;
//    private final CurrentHolder currentHolder;

    // 登录后将用户喜欢和收藏文章的id添加到缓存中
    public void loadUserLikeArticle(String userId){
        log.info("开始加载用户喜欢的文章，userId: {}", userId);
        String likeKey = USER_LIKE_BLOG_PREFIX + userId;
        LambdaQueryWrapper<UserLikeArticle> likeWrapper = new LambdaQueryWrapper<UserLikeArticle>().eq(UserLikeArticle::getUserId, userId);
        List<UserLikeArticle> userLikeLists = userLikeArticleMapper.selectList(likeWrapper);
        if(userLikeLists == null || userLikeLists.isEmpty()){
            return; // 没有喜欢的文章
        }
        for (UserLikeArticle userLikeList : userLikeLists) {
            String articleId = userLikeList.getArticleId();
            double score = userLikeList.getAddTime().toInstant(ZoneOffset.UTC).toEpochMilli();
            stringRedisTemplate.opsForZSet().add(likeKey, articleId, score);
        }
    }

    public void loadUserCollectArticle(String userId){
        log.info("开始加载用户收藏的文章，userId: {}", userId);
        String collectKey = USER_COLLECT_BLOG_PREFIX + userId;
        LambdaQueryWrapper<UserCollectArticle> collectWrapper = new LambdaQueryWrapper<UserCollectArticle>().eq(UserCollectArticle::getUserId, userId);
        List<UserCollectArticle> userCollectLists = userCollectArticleMapper.selectList(collectWrapper);
        if(userCollectLists == null || userCollectLists.isEmpty()){
            return; // 没有收藏的文章
        }
        for (UserCollectArticle userCollectList : userCollectLists) {
            String articleId = userCollectList.getArticleId();
            double score = userCollectList.getAddTime().toInstant(ZoneOffset.UTC).toEpochMilli();
            stringRedisTemplate.opsForZSet().add(collectKey, articleId, score);
        }
    }


}
