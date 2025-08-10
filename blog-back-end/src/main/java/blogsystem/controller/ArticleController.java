package blogsystem.controller;

import blogsystem.dto.NewArticleDTO;
import blogsystem.service.IArticleService;
import blogsystem.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/article")
@Tag(name = "文章相关接口")
@RequiredArgsConstructor
public class ArticleController {
    private final IArticleService articleService;

    @Operation(summary = "获取文章列表", description = "滚动分页")
    @GetMapping("/list")
    public Result articleList(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime addTime,
                                 @RequestParam Integer limit) {
        ZoneId utcZone = ZoneId.of("UTC");  // UTC时区
        ZoneId localZone = ZoneId.systemDefault();  // 或指定为 "Asia/Shanghai"

        ZonedDateTime utcDateTime = addTime.atZone(utcZone);
        LocalDateTime localDateTime = utcDateTime.withZoneSameInstant(localZone).toLocalDateTime();
        return articleService.getArticleList(localDateTime,limit);
    }

    @GetMapping("/hot/list")
    public Result hotArticleList(@RequestParam Integer limit) {
        return articleService.getHotArticleList(limit);
    }

    @GetMapping("/search")
    public Result searchArticle(@RequestParam String keyword,
                                @RequestParam Integer limit) {
        return articleService.searchArticle(keyword, limit);
    }

    @PutMapping("/like")
    public Result likeBlog(@RequestParam String articleId) {
        return articleService.likeArticle(articleId);
    }

    @PutMapping("/collect")
    public Result collectBlog(@RequestParam String articleId) {
        return articleService.collectArticle(articleId);
    }

    @GetMapping("/detail")
    public Result articleDetail(@RequestParam String articleId) {
        return articleService.getArticleDetail(articleId);
    }


    @Operation(summary = "获取文章列表", description = "通过文章类型获取文章列表")
    @GetMapping("/type/list")
    public Result articleByType(@RequestParam String typeId,
                                @RequestParam Integer limit) {
        return articleService.getArticleByType(typeId, limit);
    }

    @Operation(summary = "添加文章", description = "添加新文章")
    @PostMapping("/add")
    public Result addArticle(@RequestBody NewArticleDTO newArticleDTO) {
        return articleService.addArticle(newArticleDTO);
    }

    @Operation(summary = "保存文章", description = "临时保存草稿")
    @PostMapping("/save")
    public Result saveArticle(@RequestBody NewArticleDTO newArticleDTO) {
        return articleService.saveArticle(newArticleDTO);
    }

    @Operation(summary = "获取草稿" , description = "获取用户的草稿信息")
    @GetMapping("/draft")
    public Result getDraft() {
        return articleService.getDraft();
    }


}
