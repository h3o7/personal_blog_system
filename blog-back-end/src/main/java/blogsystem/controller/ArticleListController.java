package blogsystem.controller;

import blogsystem.dto.ArticleDTO;
import blogsystem.vo.PageResult;
import blogsystem.vo.Result;
import blogsystem.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/articleList")
@Tag(name = "文章列表接口", description = "提供文章列表的分页、删除等操作")
public class ArticleListController {
    @Autowired
    private IArticleService articleService;

    // 分页获取文章数据
    @Operation(summary = "分页获取文章列表", description = "根据条件分页获取文章数据")
    @GetMapping("/list")
    public Result getArticleList(ArticleDTO articleDto) {
        // 调用service层方法获取文章列表
        PageResult pageResult = articleService.getArticleList(articleDto);
        // 返回结果
        if (pageResult != null && pageResult.getRows() != null && !pageResult.getRows().isEmpty()) {
            return Result.success(pageResult);
        } else {
            return Result.error("没有查询到文章信息");
        }
    }

    @Operation(summary = "删除文章", description = "根据文章ID删除指定文章")
    @DeleteMapping
    public Result deleteArticle(@RequestParam String articleId) {
        // 调用service层方法删除文章
        boolean isDeleted = articleService.removeById(articleId);
        if(isDeleted){
            return Result.success("文章删除成功");
        } else {
            return Result.error("文章删除失败，可能文章不存在");
        }
    }
}
