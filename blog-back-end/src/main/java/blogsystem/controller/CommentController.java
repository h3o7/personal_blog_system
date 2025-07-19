package blogsystem.controller;

import blogsystem.entity.Comment;
import blogsystem.service.ICommentService;
import blogsystem.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comment")
@RestController
@Tag(name = "评论管理接口", description = "提供评论相关功能")
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @Operation(summary = "获取文章评论列表", description = "根据文章ID获取对应的评论列表")
    @GetMapping("/list")
    public Result articleCommentList(@RequestParam String articleId,@RequestParam Integer limit) {
        return commentService.getArticleCommentList(articleId, limit);
    }

    @Operation(summary = "获取回复评论列表", description = "根据评论ID获取对应的回复评论列表")
    @GetMapping("/reply/list")
    public Result replyCommentList(@RequestParam String commentId, @RequestParam Integer limit) {
        return commentService.getReplyCommentList(commentId, limit);
    }

    @Operation(summary = "评论", description = "对文章进行评论")
    @PostMapping("/add")
    public Result addArticleComment(@RequestBody Comment comment) {
        return commentService.addArticleComment(comment);
    }

    @Operation(summary = "回复评论", description = "对评论进行回复")
    @PostMapping("/reply/add")
    public Result addReplyComment(@RequestBody Comment comment) {
        return commentService.addReplyComment(comment);
    }

    @Operation(summary = "点赞评论", description = "对评论进行点赞")
    @PutMapping("/like")
    public Result likeComment(@RequestParam String commentId) {
        return commentService.likeComment(commentId);
    }

    @Operation(summary = "删除评论", description = "根据评论ID删除评论")
    @DeleteMapping("/delete")
    public Result deleteComment(@RequestParam String commentId) {
        return commentService.deleteComment(commentId);
    }

}
