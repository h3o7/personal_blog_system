package blogsystem.mapper;

import blogsystem.dto.AdDTO;
import blogsystem.entity.Ad;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
@Mapper
public interface AdMapper extends BaseMapper<Ad> {
    List<AdDTO> getAdList(String adTypeId);
}
