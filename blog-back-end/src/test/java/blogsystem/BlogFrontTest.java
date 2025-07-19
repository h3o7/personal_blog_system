package blogsystem;

//import org.junit.Test;
import blogsystem.entity.Article;
import blogsystem.entity.Comment;
import blogsystem.mapper.ArticleMapper;
import blogsystem.mapper.ArticleTagListMapper;
import blogsystem.mapper.CommentMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.List;
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



}
