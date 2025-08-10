package blogsystem.service.impl;

import blogsystem.dto.LoginDTO;
import blogsystem.entity.Article;
import blogsystem.entity.UserFan;
import blogsystem.mapper.*;
import blogsystem.utils.*;
import blogsystem.vo.*;
import blogsystem.entity.User;
import blogsystem.service.IUserService;
import cn.hutool.core.util.IdUtil;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static blogsystem.utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserFanMapper userFanMapper;
    private final CaptchaHandler captchaHandler;
    private final ArticleMapper articleMapper;
    private final ArticleTypeMapper articleTypeMapper;
    private final ArticleTagListMapper articleTagListMapper;
    private final AliyunOSSOperator aliyunOSSOperator;
    private final StringRedisTemplate stringRedisTemplate;
    private final LoadInfo loadInfo;


    @Override
    public PageResult getInfo(Integer pageNum, Integer pageSize, String userId, String userName) {
        Page<User> page = new Page<>(pageNum, pageSize);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        boolean hasUserId = userId != null && !userId.isEmpty();
        boolean hasUserName = userName != null && !userName.isEmpty();

        if (hasUserId && hasUserName) {
            // 两个参数都有值，使用AND连接
            queryWrapper.like("user_id", userId).like("user_name", userName);
        } else {
            // 至少一个为空，分别添加条件并用OR连接
            if (hasUserId) {
                queryWrapper.like("user_id", userId);
            }
            if (hasUserName) {
                queryWrapper.like("user_name", userName);
            }
        }

        Page<User> userPage = baseMapper.selectPage(page, queryWrapper);
        log.info(userPage.toString());
        return new PageResult(userPage.getTotal(), userPage.getRecords());
    }

    @Override
    public boolean freezeUser(String userId) {
        User user = baseMapper.selectById(userId);
        if(user != null) {
            baseMapper.freezeUser(userId);
            return true;
        }
        return false;
    }

    @Override
    public Result loginInfo(LoginDTO loginDTO) {
        // 验证验证码是否正确
        String captchaId = loginDTO.getCaptchaId();
        String code = loginDTO.getCaptchaCode();
        if (!captchaHandler.validateCaptcha(captchaId, code)) {
            return Result.error("验证码错误或已过期，请重新输入");
        }

        // 验证管理员id和密码是否正确
        User user = baseMapper.selectById(loginDTO.getId());
        if (user != null && user.getUserPassword().equals(loginDTO.getPassword())) {
            if(user.getUserFrozen() == 1) {
                return Result.error("账号已被冻结，请联系管理员");
            }
            // 如果正确，返回登录信息
            LoginVO loginVO = new LoginVO();
            loginVO.setId(user.getUserId());
            loginVO.setName(user.getUserName());
            loginVO.setLoginTime(LocalDateTime.now());
            // 生成token（这里可以使用JWT等方式生成token）
            Map<String,Object> claims = new HashMap<>();
            claims.put("id", user.getUserId());
            claims.put("name", user.getUserName());
            String token = JWTUtils.generateJWT(claims);
            loginVO.setToken(token);
            // 加载信息
            loadInfo.loadUserCollectArticle(user.getUserId());
            loadInfo.loadUserLikeArticle(user.getUserId());
            return Result.success(loginVO);
        }
        return Result.error("账号或密码错误，请重新输入");
    }

    @Override
    public Result updateUser(User newUser) throws ClientException {
        String oldUserAvatar = baseMapper.selectById(newUser.getUserId()).getUserAvatar();
        if(oldUserAvatar != null && !oldUserAvatar.isEmpty()) {
            // 如果用户头像不为空，先删除旧头像
            if(! oldUserAvatar.equals(newUser.getUserAvatar())) {
                aliyunOSSOperator.deleteFile(oldUserAvatar);
            }

        }
        // 更新用户信息
        int flag = baseMapper.updateById(newUser);
        if (flag > 0) {
            // 更新成功
            return Result.success("用户信息更新成功");
        } else {
            // 更新失败
            return Result.error("用户信息更新失败，请稍后重试");
        }
    }

    // 查询当前用户信息
    @Override
    public Result getCurrentUser(String userId) {
        User user = baseMapper.selectById(userId);
        if(user == null)
            return Result.error("当前用户不存在");
        Long fansCount = userFanMapper.selectCount(new LambdaQueryWrapper<UserFan>().eq(UserFan::getUserId, userId));
        Long followingCount = userFanMapper.selectCount(new LambdaQueryWrapper<UserFan>().eq(UserFan::getFanId,userId));
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserFans(fansCount.intValue());
        userVO.setUserFollow(followingCount.intValue());
        return Result.success(userVO);
    }

    @Override
    public Result followUser(String userId) {
        String fanId = CurrentHolder.getCurrentId();
        if (fanId.equals(userId)) {
            return Result.error("不能关注自己");
        }
        // 检查用户是否已经关注了该用户
        UserFan isMember = userFanMapper.selectOne(new LambdaQueryWrapper<UserFan>()
                .eq(UserFan::getUserId, userId)
                .eq(UserFan::getFanId, fanId));
        if(isMember != null) {
            userFanMapper.deleteById(isMember.getUserFanId());
            return Result.success("已取消关注用户");
        }
        UserFan fan = new UserFan();
        fan.setUserId(userId);
        fan.setFanId(fanId);
        fan.setUserFanId(IdUtil.simpleUUID());
        fan.setCreateTime(LocalDateTime.now());
        userFanMapper.insert(fan);
        return Result.success("已关注用户");
    }

    @Override
    public Result isFollowed(String userId) {
        String fanId = CurrentHolder.getCurrentId();
        UserFan isMember = userFanMapper.selectOne(new LambdaQueryWrapper<UserFan>()
                .eq(UserFan::getUserId, userId)
                .eq(UserFan::getFanId, fanId));
        if(isMember != null) {
            return Result.success(true);
        }
        return Result.success(false);
    }

    @Override
    public Result getLikedArticles(LocalDateTime localDateTime, Integer limit,String userId) {
        String likeKey = USER_LIKE_BLOG_PREFIX + userId;
        String nowLikeKey = USER_LIKE_BLOG_PREFIX + CurrentHolder.getCurrentId();
        String nowCollectKey = USER_COLLECT_BLOG_PREFIX + CurrentHolder.getCurrentId();
        // 从Redis中获取用户喜欢的文章列表
        try {
            // 转换 LocalDateTime 为时间戳毫秒数
            long targetTime = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();

            // 查询 score 小于目标时间戳的元素，从大到小排序，取前 limit 个
            Set<String> blogIds = stringRedisTemplate.opsForZSet()
                    .reverseRangeByScore(likeKey, 0, targetTime - 1, 0, limit);

            if (blogIds == null || blogIds.isEmpty()) {
                return Result.success(Collections.emptyList());
            }

            // 根据 ID 查询文章详情
            List<Article> articles = articleMapper.selectByIdsWithOrder(blogIds);
            List<ArticleVO> articleVOs = new ArrayList<>();

            for (Article article : articles) {
                ArticleVO articleVO = new ArticleVO();
                BeanUtils.copyProperties(article, articleVO);
                // 设置文章的用户信息
                articleVO.setArticleContent(article.getArticleContent().substring(0, Math.min(article.getArticleContent().length(), 37))); // 截取前100个字符
                articleVO.setUserName(baseMapper.selectById(article.getUserId()).getUserName());
                articleVO.setUserAvatar(baseMapper.selectById(article.getUserId()).getUserAvatar());
                articleVO.setArticleType(articleTypeMapper.selectById(article.getArticleTypeId()).getArticleTypeName());
                List<String> tags = articleTagListMapper.selectTagsById(article.getArticleId());
                articleVO.setArticleTags(tags);
                // 标记为已喜欢
                Double scoreLike = stringRedisTemplate.opsForZSet().score(nowLikeKey, article.getArticleId());
                if (scoreLike != null) {
                    articleVO.setLiked(true);
                }
                Double scoreCollect = stringRedisTemplate.opsForZSet().score(nowCollectKey, article.getArticleId());
                if (scoreCollect != null) {
                    articleVO.setCollected(true);
                }
                articleVOs.add(articleVO);
            }

            // 返回文章列表
            return Result.success(articleVOs);
        } catch (Exception e) {
            log.error("获取用户喜欢文章列表失败", e);
            return Result.error("获取喜欢的文章列表失败");
        }
    }

    @Override
    public Result getCollectedArticles(LocalDateTime localDateTime, Integer limit,String userId) {
        String collectKey = USER_COLLECT_BLOG_PREFIX + userId;
        String nowLikeKey = USER_LIKE_BLOG_PREFIX + CurrentHolder.getCurrentId();
        String nowCollectKey = USER_COLLECT_BLOG_PREFIX + CurrentHolder.getCurrentId();
        // 从Redis中获取用户喜欢的文章列表
        try {
            // 转换 LocalDateTime 为时间戳毫秒数
            long targetTime = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();

            // 查询 score 小于目标时间戳的元素，从大到小排序，取前 limit 个
            Set<String> blogIds = stringRedisTemplate.opsForZSet()
                    .reverseRangeByScore(collectKey, 0, targetTime - 1, 0, limit);

            if (blogIds == null || blogIds.isEmpty()) {
                return Result.success(Collections.emptyList());
            }

            // 根据 ID 查询文章详情
            List<Article> articles = articleMapper.selectByIdsWithOrder(blogIds);
            List<ArticleVO> articleVOs = new ArrayList<>();

            for (Article article : articles) {
                ArticleVO articleVO = new ArticleVO();
                BeanUtils.copyProperties(article, articleVO);
                // 设置文章的用户信息
                articleVO.setArticleContent(article.getArticleContent().substring(0, Math.min(article.getArticleContent().length(), 37))); // 截取前100个字符
                articleVO.setUserName(baseMapper.selectById(article.getUserId()).getUserName());
                articleVO.setUserAvatar(baseMapper.selectById(article.getUserId()).getUserAvatar());
                articleVO.setArticleType(articleTypeMapper.selectById(article.getArticleTypeId()).getArticleTypeName());
                List<String> tags = articleTagListMapper.selectTagsById(article.getArticleId());
                articleVO.setArticleTags(tags);
                // 标记为已喜欢
                Double scoreLike = stringRedisTemplate.opsForZSet().score(nowLikeKey, article.getArticleId());
                if (scoreLike != null) {
                    articleVO.setLiked(true);
                }
                Double scoreCollect = stringRedisTemplate.opsForZSet().score(nowCollectKey, article.getArticleId());
                if (scoreCollect != null) {
                    articleVO.setCollected(true);
                }
                articleVOs.add(articleVO);
            }

            // 返回文章列表
            return Result.success(articleVOs);
        } catch (Exception e) {
            log.error("获取用户喜欢收藏列表失败", e);
            return Result.error("获取收藏的文章列表失败");
        }
    }



    @Override
    public Result getFollowingUsers(Integer pageNum, Integer pageSize) {
        String userId = CurrentHolder.getCurrentId();
        Page<UserFan> page = new Page<>(pageNum, pageSize);
        // 查询当前用户关注的用户列表
        Page<UserFan> userFanPage = userFanMapper.selectPage(page, new QueryWrapper<UserFan>().eq("fan_id", userId).orderByDesc("create_time"));
        List<UserFan> userFans = userFanPage.getRecords();
        List<UserVO> userVOs = new ArrayList<>();
        for (UserFan userFan : userFans) {
            User user = baseMapper.selectById(userFan.getUserId());
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                userVO.setFollowing(true);
                userVOs.add(userVO);
            }
        }
        return Result.success(userVOs);
    }

    @Override
    public Result getFollowers(Integer pageNum, Integer pageSize) {
        String userId = CurrentHolder.getCurrentId();
        Page<UserFan> page = new Page<>(pageNum, pageSize);
        // 查询当前用户的粉丝列表
        Page<UserFan> userFanPage = userFanMapper.selectPage(page, new QueryWrapper<UserFan>().eq("user_id", userId).orderByDesc("create_time"));
        List<UserFan> userFans = userFanPage.getRecords();
        List<UserVO> userVOs = new ArrayList<>();
        for (UserFan userFan : userFans) {
            User user = baseMapper.selectById(userFan.getFanId());
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
//                userVO.setUserId();
                UserFan member = userFanMapper.selectOne(new LambdaQueryWrapper<UserFan>()
                        .eq(UserFan::getUserId, user.getUserId())
                        .eq(UserFan::getFanId, userId));
                if (member != null) {
                    userVO.setFollowing(true);
                } else {
                    userVO.setFollowing(false);
                }
                userVOs.add(userVO);
            }
        }
        return Result.success(userVOs);
    }

    @Override
    public Result getMyArticles(Integer pageNum, Integer pageSize) {
        String userId = CurrentHolder.getCurrentId();
        String likeKey = USER_LIKE_BLOG_PREFIX + userId;
        String collectKey = USER_COLLECT_BLOG_PREFIX + userId;
        Page<Article> page = new Page<>(pageNum, pageSize);

        // 查询当前用户的文章列表
        Page<Article> articlePage = articleMapper.selectPage(page, new QueryWrapper<Article>().eq("user_id", userId).orderByDesc("article_add_time"));
        List<Article> articles = articlePage.getRecords();
        List<ArticleVO> articleVOs = new ArrayList<>();
        for (Article article : articles) {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article, articleVO);
            // 设置文章的用户信息
            articleVO.setArticleContent(article.getArticleContent().substring(0, Math.min(article.getArticleContent().length(), 37))); // 截取前100个字符
            articleVO.setUserName(baseMapper.selectById(article.getUserId()).getUserName());
            articleVO.setUserAvatar(baseMapper.selectById(article.getUserId()).getUserAvatar());
            articleVO.setArticleType(articleTypeMapper.selectById(article.getArticleTypeId()).getArticleTypeName());
            List<String> tags = articleTagListMapper.selectTagsById(article.getArticleId());
            articleVO.setArticleTags(tags);
            // 标记为已喜欢
            Double scoreLike = stringRedisTemplate.opsForZSet().score(likeKey, article.getArticleId());
            if (scoreLike != null) {
                articleVO.setLiked(true);
            }
            Double scoreCollect = stringRedisTemplate.opsForZSet().score(collectKey, article.getArticleId());
            if (scoreCollect != null) {
                articleVO.setCollected(true);
            }
            articleVOs.add(articleVO);
        }
        return Result.success(articleVOs);

    }

    private String hideMiddleStr(String str){
        if(str.length() < 2)
            return str.charAt(0) + "**" + str.charAt(0);
        return str.charAt(0) + "**" + str.charAt(str.length() - 1);
    }
}
