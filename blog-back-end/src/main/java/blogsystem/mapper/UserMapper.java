package blogsystem.mapper;

import blogsystem.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    void freezeUser(String userId);

}
