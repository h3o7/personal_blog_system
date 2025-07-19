package blogsystem.service;

import blogsystem.entity.AdType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
public interface IAdTypeService extends IService<AdType> {

    boolean add(AdType adType);

    boolean deleteById(String adTypeId);
}
