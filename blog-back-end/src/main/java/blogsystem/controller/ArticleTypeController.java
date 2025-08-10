package blogsystem.controller;

import blogsystem.dto.ArticleTypeDTO;
import blogsystem.vo.Result;
import blogsystem.entity.ArticleType;
import blogsystem.service.IArticleTypeService;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/articleType")
@Tag(name = "文章类型接口", description = "提供文章类型的增删改查等操作")
@Slf4j
@RequiredArgsConstructor
public class ArticleTypeController {

    private final IArticleTypeService articleTypeService;

    // 获取所有文章类型，并按article_type_sort升序排序
    @Operation(summary = "获取所有文章类型", description = "获取所有文章类型，并按排序字段升序排列")
    @RequestMapping("/getAll")
    public Result getAll(){
        try {
            return Result.success(articleTypeService.getAll());
        } catch (Exception e) {
            log.error("获取文章类型失败: {}", e.getMessage());
            return Result.error("获取文章类型失败");
        }
    }

    @Operation(summary = "编辑文章类型", description = "根据类型ID更新文章类型信息")
    @PutMapping("/edit")
    public Result editArticleType(@RequestBody ArticleTypeDTO articleTypeDto) {
        // 将获取的请求体转换成 ArticleTypeDto对象
        // 再将其数据传递给ArticleTypeService进行编辑操作
        ArticleType articleType = new ArticleType();
        BeanUtils.copyProperties(articleTypeDto, articleType);
        try {
            boolean result = articleTypeService.updateById(articleType);
            if (result) {
                return Result.success("文章类型已更新");
            } else {
                return Result.error("更新失败，文章类型不存在或信息未更改");
            }
        } catch (Exception e) {
            log.error("编辑文章类型失败: {}", e.getMessage());
            return Result.error("编辑文章类型失败");
        }
    }

    @Operation(summary = "添加文章类型", description = "添加新的文章类型")
    @PostMapping("/add")
    public Result addArticleType(@RequestBody ArticleTypeDTO articleTypeDto) {
        ArticleType articleType = new ArticleType();
        BeanUtils.copyProperties(articleTypeDto, articleType);
        articleType.setArticleTypeAddTime(LocalDateTime.now());
        articleType.setArticleTypeId(IdUtil.simpleUUID());
        try {
            boolean result = articleTypeService.save(articleType);
            if (result) {
                return Result.success("文章类型已添加");
            } else {
                return Result.error("添加失败，文章类型信息不完整或已存在");
            }
        } catch (Exception e) {
            log.error("添加文章类型失败: {}", e.getMessage());
            return Result.error("添加文章类型失败");
        }
    }

    @Operation(summary = "删除文章类型", description = "根据类型ID删除文章类型")
    @DeleteMapping
    public Result deleteArticleType(@RequestParam String articleTypeId) {
        // 根据文章类型id删除
        try {
            boolean result = articleTypeService.removeById(articleTypeId);
            if (result) {
                return Result.success("文章类型已删除");
            } else {
                return Result.error("删除失败，文章类型不存在或已被删除");
            }
        } catch (Exception e) {
            log.error("删除文章类型失败: {}", e.getMessage());
            return Result.error("删除文章类型失败");
        }
    }

}
