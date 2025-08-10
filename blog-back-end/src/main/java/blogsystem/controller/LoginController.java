package blogsystem.controller;

import blogsystem.dto.LoginDTO;
import blogsystem.service.IUserService;
import blogsystem.utils.CaptchaHandler;
import blogsystem.vo.Result;
import blogsystem.service.IAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "登录接口", description = "提供登录功能")
public class LoginController {
    private final IAdminService adminService;
    private final IUserService userService;
    private final CaptchaHandler captchaHandler;

    @Operation(summary = "获取验证码", description = "生成验证码并返回图片和验证码文本")
    @GetMapping("/captcha")
    public void generateCaptcha(HttpServletResponse response) throws IOException {
        String captchaId = captchaHandler.generateCaptcha(response);
        if (captchaId == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "验证码生成失败");
        } else {
            response.setHeader("captcha-id", captchaId);
            response.setHeader("Access-Control-Expose-Headers", "captcha-id");
        }
    }


    @Operation(summary = "管理员登录", description = "使用管理员账号和密码进行登录，返回登录信息")
    @PostMapping("/admin")
    public Result loginAdmin(@RequestBody LoginDTO loginDTO) {
        return adminService.loginInfo(loginDTO);

    }

    @Operation(summary = "用户登录", description = "使用用户账号和密码进行登录，返回登录信息")
    @PostMapping("/user")
    public Result loginUser(@RequestBody LoginDTO loginDTO) {
        return userService.loginInfo(loginDTO);
    }

}
