package blogsystem.service.impl;

import blogsystem.dto.LoginDTO;
import blogsystem.entity.Admin;
import blogsystem.mapper.AdminMapper;
import blogsystem.service.IAdminService;
import blogsystem.utils.CaptchaHandler;
import blogsystem.vo.LoginVO;
import blogsystem.vo.Result;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import blogsystem.utils.JWTUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private CaptchaHandler captchaHandler;

    @Override
    public Result loginInfo(LoginDTO loginDTO) {
        // 验证验证码是否正确
        String captchaId = loginDTO.getCaptchaId();
        String code = loginDTO.getCaptchaCode();
        if (!captchaHandler.validateCaptcha(captchaId, code)) {
            return Result.error("验证码错误或已过期，请重新输入");
        }

        // 验证管理员id和密码是否正确
        Admin admin = adminMapper.selectById(loginDTO.getId());
        if (admin != null && admin.getAdminPassword().equals(loginDTO.getPassword())) {
            // 如果正确，返回登录信息
            LoginVO loginVO = new LoginVO();
            loginVO.setId(admin.getAdminId());
            loginVO.setName(admin.getAdminName());
            loginVO.setLoginTime(LocalDateTime.now());
            // 生成token（这里可以使用JWT等方式生成token）
            Map<String,Object> claims = new HashMap<>();
            claims.put("id", admin.getAdminId());
            claims.put("name", admin.getAdminName());
            String token = JWTUtils.generateJWT(claims);
            loginVO.setToken(token);
            return Result.success(loginVO);
        }
        return Result.error("账号或密码错误，请重新输入");
    }
}
