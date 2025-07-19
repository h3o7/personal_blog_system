package blogsystem.controller;

import blogsystem.dto.LoginDTO;
import blogsystem.entity.User;
import blogsystem.service.IUserService;
import blogsystem.utils.CaptchaHandler;
import blogsystem.vo.Result;
import blogsystem.entity.Admin;
import blogsystem.service.IAdminService;
import blogsystem.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/login")
@Tag(name = "登录接口", description = "提供登录功能")
public class LoginController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private IUserService userService;
    @Autowired
    private CaptchaHandler captchaHandler;

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
