package blogsystem.controller;

import blogsystem.vo.Result;
import blogsystem.entity.ArticleTag;
import blogsystem.service.IArticleTagService;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/articleTag")
@Tag(name = "文章标签接口", description = "提供文章标签的增删改查等操作")
public class ArticleTagController {
    @Autowired
    private IArticleTagService articleTagService;

    @Operation(summary = "获取标签列表", description = "获取所有文章标签，按添加时间降序排序")
    @GetMapping("/list")
    public Result getInfo(){
        // 利用MybatisPlus里的条件控制器使查询出的标签按照时间降序排序
        QueryWrapper<ArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("article_tag_add_time");
        List<ArticleTag> articleTagList = articleTagService.list(queryWrapper);
        if (articleTagList != null && !articleTagList.isEmpty()) {
            return Result.success(articleTagList);
        } else {
            return Result.error("没有查询到标签信息");
        }
    }

    @Operation(summary = "删除标签", description = "根据标签ID删除指定标签")
    @DeleteMapping
    public Result deleteArticleTag(@RequestParam String articleTagId) {
        boolean result = articleTagService.deleteById(articleTagId);
        if(result){
            return Result.success("标签删除成功");
        } else {
            return Result.error("标签删除失败，可能是标签不存在或已被使用");
        }
    }

    @Operation(summary = "更新标签", description = "根据标签ID更新标签信息")
    @PutMapping("/update")
    public Result updateArticleTag(@RequestBody ArticleTag articleTag) {
        articleTag.setArticleTagAddTime(LocalDateTime.now());
        boolean result = articleTagService.updateById(articleTag);
        if(result){
            return Result.success("标签更新成功");
        } else {
            return Result.error("标签更新失败，可能是标签不存在或信息未更改");
        }
    }

    @Operation(summary = "添加标签", description = "添加新的文章标签")
    @PostMapping("/add")
    public Result addArticleTag(@RequestBody ArticleTag articleTag) {
        articleTag.setArticleTagAddTime(LocalDateTime.now());
        articleTag.setArticleTagId(IdUtil.simpleUUID());
        boolean result = articleTagService.save(articleTag);
        if(result){
            return Result.success("标签添加成功");
        } else {
            return Result.error("标签添加失败，可能是标签已存在");
        }
    }


}
