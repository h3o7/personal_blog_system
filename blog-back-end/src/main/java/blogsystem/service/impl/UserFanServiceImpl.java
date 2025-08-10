package blogsystem.service.impl;

import blogsystem.entity.UserFan;
import blogsystem.mapper.UserFanMapper;
import blogsystem.service.IUserFanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户粉丝表 服务实现类
 * </p>
 *
 * @author h3o7
 * @since 2025-08-09
 */
@Service
public class UserFanServiceImpl extends ServiceImpl<UserFanMapper, UserFan> implements IUserFanService {

}
