package blogsystem.controller;


import blogsystem.vo.Result;
import blogsystem.entity.Link;
import blogsystem.service.ILinkService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/link")
@Tag(name = "友情链接接口", description = "提供友情链接的增删改查等操作")
public class LinkController {
    @Autowired
    private ILinkService linkService;

    @Operation(summary = "获取友情链接列表", description = "获取所有友情链接，按链接排序")
    @GetMapping("/list")
    public Result getInfo(){
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("link_sort");
        List<Link> list =  linkService.list(queryWrapper);
        if (list.isEmpty()) {
            return Result.error("没有友情链接");
        } else {
            return Result.success(list);
        }
    }

    @Operation(summary = "获取友情链接详情", description = "根据链接ID获取友情链接的详细信息")
    @DeleteMapping
    public Result delete(@RequestParam String linkId){
        boolean isDeleted = linkService.removeById(linkId);
        if (isDeleted) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败，可能是链接不存在");
        }
    }

    @Operation(summary = "添加友情链接", description = "添加新的友情链接")
    @PostMapping("/add")
    public Result save(@RequestBody Link link){
        boolean isAdd = linkService.add(link);
        if (isAdd) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败，可能是链接已存在");
        }
    }

    @Operation(summary = "编辑友情链接", description = "根据链接ID更新友情链接信息")
    @PutMapping("/edit")
    public Result update(@RequestBody Link link) {
        boolean isUpdated = linkService.updateById(link);
        if (isUpdated) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败，可能是链接不存在");
        }
    }
}
