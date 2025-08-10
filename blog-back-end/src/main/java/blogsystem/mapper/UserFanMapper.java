package blogsystem.mapper;

import blogsystem.entity.UserFan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户粉丝表 Mapper 接口
 * </p>
 *
 * @author h3o7
 * @since 2025-07-15
 */
@Mapper
public interface UserFanMapper extends BaseMapper<UserFan> {

}
