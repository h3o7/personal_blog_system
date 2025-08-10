package blogsystem.controller;

import blogsystem.dto.AdminDTO;
import blogsystem.vo.Result;
import blogsystem.service.IAdTypeService;
import blogsystem.service.IArticleService;
import blogsystem.service.IArticleTagService;
import blogsystem.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.SystemPropsUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/admin")
@Tag(name = "后台管理接口", description = "提供后台管理相关的统计和信息接口")
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final IArticleService articleService;
    private final IArticleTagService articleTagService;
    private final IAdTypeService adTypeService;
    private final IUserService userService;

    @Operation(summary = "获取后台统计信息", description = "获取文章、标签、广告类型、用户等后台统计数据")
    @GetMapping
    @ResponseBody
    public Result admin() {

        AdminDTO adminDTO = new AdminDTO(
                (int) articleService.count(),
                (int) articleTagService.count(),
                (int) adTypeService.count(),
                (int) userService.count(),
                // 利用hutool包获取系统IP和名称
                SystemPropsUtil.get("os.name") + " " + SystemPropsUtil.get("os.version"),
                NetUtil.getLocalhost().getHostAddress()
        );

        return Result.success(adminDTO);
    }
}
