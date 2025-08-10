package blogsystem.service.impl;

import blogsystem.entity.Comment;
import blogsystem.entity.CommentReply;
import blogsystem.mapper.CommentMapper;
import blogsystem.mapper.CommentReplyMapper;
import blogsystem.mapper.UserMapper;
import blogsystem.service.ICommentService;
import blogsystem.utils.CurrentHolder;
import blogsystem.vo.CommentVO;
import blogsystem.vo.Result;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static blogsystem.utils.RedisConstants.USER_LIKE_COMMENT_PREFIX;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentReplyMapper commentReplyMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result getArticleCommentList(String articleId, Integer limit) {
        // 根据热度对评论进行排序
        List<Comment> list = commentMapper.selectHotCommentsByArticleId(articleId, limit);
        String userId = CurrentHolder.getCurrentId();
        String key = USER_LIKE_COMMENT_PREFIX + userId;

        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : list) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            String userName = userMapper.selectById(comment.getUserId()).getUserName();
            String userAvatar = userMapper.selectById(comment.getUserId()).getUserAvatar();
            commentVO.setUserName(userName);
            commentVO.setUserAvatar(userAvatar);
            // 检查是否已点赞
            Boolean isLiked = stringRedisTemplate.opsForSet().isMember(key, comment.getCommentId());
            commentVO.setLiked(Boolean.TRUE.equals(isLiked));
            commentVOList.add(commentVO);
        }
        return Result.success(commentVOList);
    }

    @Override
    public Result getReplyCommentList(String commentId, Integer limit) {
        List<Comment> list = commentMapper.selectHotCommentsByCommentId(commentId, limit);
        String userId = CurrentHolder.getCurrentId();
        String key = USER_LIKE_COMMENT_PREFIX + userId;

        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : list) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            String userName = userMapper.selectById(comment.getUserId()).getUserName();
            String userAvatar = userMapper.selectById(comment.getUserId()).getUserAvatar();
            String replyUserId = commentReplyMapper.selectOne(new LambdaQueryWrapper<CommentReply>()
                    .eq(CommentReply::getSecondCommentId,comment.getCommentId())).getReplyUserId();
            String replyUserName = userMapper.selectById(replyUserId).getUserName();
            commentVO.setUserName(userName);
            commentVO.setUserAvatar(userAvatar);
            commentVO.setRepliedName(replyUserName);
            // 检查是否已点赞
            Boolean isLiked = stringRedisTemplate.opsForSet().isMember(key, comment.getCommentId());
            commentVO.setLiked(Boolean.TRUE.equals(isLiked));
            commentVOList.add(commentVO);
        }
        return Result.success(commentVOList);
    }

    @Override
    public Result addArticleComment(Comment comment) {
        String userId = CurrentHolder.getCurrentId();
        comment.setCommentId(IdUtil.simpleUUID());
        comment.setUserId(userId);
        comment.setCommentGoodNumber(0);
        comment.setCommentTime(LocalDateTime.now());
        commentMapper.insert(comment);
        return Result.success("评论成功");
    }

    @Override
    @Transactional
    public Result addReplyComment(Comment comment) {
        // 添加回复评论
        CommentReply commentReply = new CommentReply();
        commentReply.setCommentReplyId(IdUtil.simpleUUID());
        commentReply.setFirstCommentId(comment.getCommentId());
        // 设置回复的评论ID
        commentReply.setSecondCommentId(IdUtil.simpleUUID());
        commentReply.setReplyUserId(comment.getUserId());
        commentReplyMapper.insert(commentReply);

        String userId = CurrentHolder.getCurrentId();
        comment.setCommentId(commentReply.getSecondCommentId());
        comment.setUserId(userId);
        comment.setCommentGoodNumber(0);
        comment.setCommentTime(LocalDateTime.now());
        commentMapper.insert(comment);
        return Result.success("回复成功");
    }

    @Override
    public Result likeComment(String commentId) {
        String userId = CurrentHolder.getCurrentId();
        String key = USER_LIKE_COMMENT_PREFIX + userId;
        // 检查用户是否已点赞
        Boolean isLiked = stringRedisTemplate.opsForSet().isMember(key, commentId);
        if (Boolean.TRUE.equals(isLiked)) {
            // 如果已点赞，则取消点赞
            stringRedisTemplate.opsForSet().remove(key, commentId);
            UpdateWrapper<Comment> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("comment_id", commentId)
                        .setSql("comment_good_number = comment_good_number - 1");
            int update = commentMapper.update(null, updateWrapper);
            if (update == 0) {
                return Result.error("取消点赞失败，评论可能已被删除或不存在");
            }
            return Result.success("取消点赞成功");
        } else {
            // 如果未点赞，则添加点赞
            stringRedisTemplate.opsForSet().add(key, commentId);
            UpdateWrapper<Comment> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("comment_id", commentId)
                    .setSql("comment_good_number = comment_good_number + 1");
            int update = commentMapper.update(null, updateWrapper);
            if (update == 0) {
                return Result.error("点赞失败，评论可能已被删除或不存在");
            }
            return Result.success("点赞成功");
        }
    }

    @Override
    @Transactional
    public Result deleteComment(String commentId) {
        // 判断当前用户能不能删除评论
        String userId = CurrentHolder.getCurrentId();
        Comment comment = commentMapper.selectById(commentId);
        if(! comment.getUserId().equals(userId)){
            return Result.error("你没有权限删除该评论");
        }
        // 删除评论
        int deleteCount = commentMapper.deleteById(commentId);
        if (deleteCount > 0) {
            // 删除评论回复
            LambdaQueryWrapper<CommentReply> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CommentReply::getFirstCommentId, commentId)
                        .or().eq(CommentReply::getSecondCommentId, commentId);
            commentReplyMapper.delete(queryWrapper);
        }
        String key = USER_LIKE_COMMENT_PREFIX + userId;
        // 从Redis中删除用户点赞记录
        stringRedisTemplate.opsForSet().remove(key, commentId);
        return Result.success("删除评论成功");
    }
}
