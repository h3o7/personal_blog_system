package blogsystem.controller;


import blogsystem.entity.User;
import blogsystem.service.IUserService;
import cn.hutool.core.lang.Console;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
@Tag(name = "首页接口", description = "提供首页相关的操作接口")
public class IndexController {
    @Autowired
    private IUserService userService;

    @Operation(summary = "首页测试接口", description = "测试首页服务是否正常，返回ok")
    @GetMapping("/")
    @ResponseBody
    public String index() {
        for(User user : userService.list()) {
//            log.info("User: {}", user);
            Console.log(user);
        }
        return "ok";
    }
}
