package blogsystem;

import blogsystem.entity.*;
import blogsystem.mapper.UserMapper;
import blogsystem.service.*;
import blogsystem.utils.AliyunOSSOperator;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.IdUtil;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
class BlogSystemApplicationTests {

    @Autowired
    IUserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IArticleTypeService articleTypeService;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private IArticleTagService articleTagService;
    @Autowired
    private IArticleTagListService articleTagListService;
    @Autowired
    private ILinkService linkService;
    @Autowired
    private IAdTypeService adTypeService;
    @Autowired
    private IAdService adService;
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;


    @Test
    void test() {
        // 测试MybitsPlus的分页查询是否正常
        // 每页查询2条数据
        // Page对象的构造函数参数分别为当前页码和每页条数
        Page<User> page = new Page<>(1, 2);
        // 调用IUserService的page方法进行分页查询
        Page<User> userPage = userService.page(page);
        for (User user : userPage.getRecords()) {
            System.out.println(user.getUserId() + " " + user.getUserName());
            // 输出查询结果
//            System.out.print("用户ID：" + user.getUserId() + " ");
//            System.out.println(user);
//            System.out.println("邮箱：" + user.getEmail());
//            System.out.print("注册时间：" + user.getCreateTime());
            System.out.println("-----------------------------");
        }
        // 输出查询结果
//        System.out.println("当前页码：" + userPage.getCurrent());
//        System.out.println("每页条数：" + userPage.getSize());
//        System.out.println("总页数：" + userPage.getPages());
//
//        System.out.println("总记录数：" + userPage.getTotal());
//        System.out.println("查询结果：");


    }
    @Test
    void userLoads() {
        // 给表user批量添加一百条数据
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setUserId(i + "user");
            user.setUserName(i + "UName");
            // 利用hutool包对密码加密
            user.setUserPassword("123456"); // 密码123456
            user.setUserFrozen(0); // 0:正常
            user.setUserRegisterTime(LocalDateTime.now().minusMonths(i)); // 注册时间
            users.add(user);

        }
        // 批量插入数据
        userService.saveBatch(users);
    }
    @Test
    void addArticleData() {
        // 添加文章类型数据
        ArrayList<ArticleType> articleTypes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ArticleType articleType = new ArticleType();
            articleType.setArticleTypeId("" + i);
            articleType.setArticleTypeName("文章类型" + i);
            articleType.setArticleTypeSort(i + 1);
            articleType.setArticleTypeAddTime(LocalDateTime.now().minusMonths(i * 7));
            articleTypes.add(articleType);
        }
        articleTypeService.saveBatch(articleTypes);

        List<User> users = userService.list();
        // 添加文章数据
        ArrayList<Article> articles = new ArrayList<>();
        int count = 0;
        for (ArticleType articleType : articleTypes) {
            for (int i = 0; i < 6; i++) {
                count++;
                Article article = new Article();
                article.setArticleId(count + "");
                article.setArticleTypeId(articleType.getArticleTypeId());
                article.setUserId(users.get(ThreadLocalRandom.current().nextInt(users.size())).getUserId());
                article.setArticleTitle("文章标题" + i + " - " + articleType.getArticleTypeName());
                article.setArticleAddTime(LocalDateTime.now().minusDays(i + 1));
                article.setArticleContent("文章内容" + "i");
                article.setArticleGoodNumber(0); // 点赞次数随机
                article.setArticleLookNumber(0); // 阅读次数随机
                article.setArticleCollectionNumber(0); // 收藏次数随机
                articles.add(article);
            }
        }
        articleService.saveBatch(articles, 50);

        // 添加文章标签数据
        ArrayList<ArticleTag> articleTags = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleTagId("" + i);
            articleTag.setArticleTagName("文章标签" + i);
            articleTag.setArticleTagAddTime(LocalDateTime.now().minusDays(i + 1));
            articleTags.add(articleTag);
        }
        articleTagService.saveBatch(articleTags, 50);
        // 添加文章标签列表数据
        ArrayList<ArticleTagList> articleTagLists = new ArrayList<>();
        count = 0;
        for (Article article : articles) {
            count++;
            ArticleTagList articleTagList = new ArticleTagList();
            articleTagList.setArticleTagListId(count + "");
            articleTagList.setArticleId(article.getArticleId());
            articleTagList.setArticleTagId(articleTags.get(ThreadLocalRandom.current().nextInt(articleTags.size())).getArticleTagId());
            articleTagLists.add(articleTagList);
        }
        articleTagListService.saveBatch(articleTagLists, 50);
    }
    @Test
    void contextLoads() {
    }
    @Test
    void testPage() {
        Page<User> page = new Page<>(1, 10);
        // 如果需要根据userId或userName进行查询，可以在这里添加查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", "1");

        // 执行分页查询
        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        for (User user : userPage.getRecords()) {
            Console.log(user.getUserId() + " " + user.getUserName());
        }

    }
    @Test
    void testArticleTypeSort() {

    }
    @Test
    void testArticleTagSortDesc() {
        // 获取所有文章类型，并按article_type_sort降序排序
        List<ArticleTag> articleTypes = articleTagService.list(new QueryWrapper<ArticleTag>().orderByDesc("article_tag_add_time"));
        for (ArticleTag articleTag : articleTypes) {
            Console.log(articleTag.getArticleTagId() + " " + articleTag.getArticleTagName() + " " + articleTag.getArticleTagAddTime());
        }
    }
    @Test
    void addLink() {
        // 添加友情链接数据
        ArrayList<Link> links = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Link link = new Link();
            link.setLinkId(IdUtil.simpleUUID());
            link.setLinkTitle("百度" + i);
            link.setLinkUrl("https://baidu.com");
            link.setLinkLogoUrl("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
            link.setLinkSort(i + 1);
            link.setLinkAddTime(LocalDateTime.now().minusDays(i));
            links.add(link);
        }
        // 批量插入数据
        linkService.saveBatch(links);
    }


    @Test
    void testAliyunOSS() throws ClientException {

    }



}
