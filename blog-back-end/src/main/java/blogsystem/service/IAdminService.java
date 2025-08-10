package blogsystem.service;

import blogsystem.dto.LoginDTO;
import blogsystem.entity.Admin;
import blogsystem.vo.LoginVO;
import blogsystem.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IAdminService extends IService<Admin> {


    Result loginInfo(LoginDTO loginDTO);
}
