package blogsystem;

//import org.junit.Test;
import blogsystem.entity.Article;
import blogsystem.entity.Comment;
import blogsystem.entity.UserLikeArticle;
import blogsystem.mapper.*;
import blogsystem.service.IUserCollectArticleService;
import blogsystem.service.IUserLikeArticleService;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static blogsystem.utils.RedisConstants.USER_COLLECT_BLOG_PREFIX;
import static blogsystem.utils.RedisConstants.USER_LIKE_BLOG_PREFIX;

@SpringBootTest
public class BlogFrontTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagListMapper articleTagListMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private IUserLikeArticleService userLikeArticleService;
    @Autowired
    private IUserCollectArticleService userCollectArticleService;

    @Test
    // 测试Redis连接
    void testRedisConnection() {
        // 检查Redis连接是否成功
//        stringRedisTemplate.
        // 设置一个键值对
        stringRedisTemplate.opsForValue().set("testKey", "123456");



        // 获取并打印该键的值
        String value = stringRedisTemplate.opsForValue().get("testKey");
        System.out.println("Redis testKey value: " + value);
    }

    @Test
    void testBlogList(){
        LocalDateTime addTime = LocalDateTime.now();
        Integer count = 10;

        List<Article> list = articleMapper.selectList(new LambdaQueryWrapper<Article>().
                lt(Article::getArticleAddTime, addTime)
                .orderByDesc(Article::getArticleAddTime)
                .last("LIMIT " + count));

        for (Article article : list) {
            System.out.println(article);
        }
    }

    @Test
    void addRedis() {
        String likeKey = USER_LIKE_BLOG_PREFIX + "13user";
        String collectKey = USER_COLLECT_BLOG_PREFIX + "13user";
        // 添加数据到Redis
        stringRedisTemplate.opsForSet().add(likeKey, "37");
//        stringRedisTemplate.opsForSet().add(collectKey, "2", "3","21");

    }

    @Test
    void changeTime(){
        // 将时间戳转变成当前时间
//        LocalDateTime localDateTime = LocalDateTime.now();
        long time1 = 1752677827395L;
        long time2 = 1752677812976L;
        // 将时间戳转换为LocalDateTime
        LocalDateTime localDateTime1 = LocalDateTime.ofEpochSecond(time1 / 1000, (int) (time1 % 1000) * 1000000, java.time.ZoneOffset.UTC);
        LocalDateTime localDateTime2 = LocalDateTime.ofEpochSecond(time2 / 1000, (int) (time2 % 1000) * 1000000, java.time.ZoneOffset.UTC);
        // 打印结果
        System.out.println("LocalDateTime1: " + localDateTime1);
        System.out.println("LocalDateTime2: " + localDateTime2);


    }

    @Test
    void testRange(){
        String collectKey = USER_COLLECT_BLOG_PREFIX + "3user";
        Set<String> blogIds = stringRedisTemplate.opsForZSet()
                .reverseRangeByScore(collectKey, 0, System.currentTimeMillis(), 0, 10);
        blogIds.forEach(System.out::println);
    }

    @Test
    void printComment(){
        List<Comment> list = commentMapper.selectHotCommentsByCommentId("c1", 10);
        list.forEach(System.out::println);
    }

    @Test
    void dataPersistenceLike(){
        String likeKey = USER_LIKE_BLOG_PREFIX + "37user";
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().rangeWithScores(likeKey, 0, -1);
        List<UserLikeArticle> list = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
            UserLikeArticle userLikeArticle = new UserLikeArticle();
            userLikeArticle.setUserLikeArticleId(IdUtil.simpleUUID());
            userLikeArticle.setUserId("37user");
            userLikeArticle.setArticleId(typedTuple.getValue());

            long score = Objects.requireNonNull(typedTuple.getScore()).longValue(); // 假设 score 为毫秒时间戳
            LocalDateTime likeTime = LocalDateTime.ofEpochSecond(score / 1000, (int) (score % 1000) * 1000000, java.time.ZoneOffset.UTC);
            userLikeArticle.setAddTime(likeTime);
            list.add(userLikeArticle);
        }
        userLikeArticleService.saveBatch(list);
    }

    @Test
    void dataPersistenceCollect(){
        String collectKey = USER_COLLECT_BLOG_PREFIX + "37user";
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().rangeWithScores(collectKey, 0, -1);
        List<blogsystem.entity.UserCollectArticle> list = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
            blogsystem.entity.UserCollectArticle userCollectArticle = new blogsystem.entity.UserCollectArticle();
            userCollectArticle.setUserCollectArticleId(IdUtil.simpleUUID());
            userCollectArticle.setUserId("37user");
            userCollectArticle.setArticleId(typedTuple.getValue());
            long score = Objects.requireNonNull(typedTuple.getScore()).longValue(); // 假设 score 为毫秒时间戳
            LocalDateTime collectTime = LocalDateTime.ofEpochSecond(score / 1000, (int) (score % 1000) * 1000000, java.time.ZoneOffset.UTC);
            userCollectArticle.setAddTime(collectTime);
            list.add(userCollectArticle);
        }
        userCollectArticleService.saveBatch(list);
    }



}
