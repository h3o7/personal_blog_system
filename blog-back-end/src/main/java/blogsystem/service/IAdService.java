package blogsystem.service;

import blogsystem.dto.AdDTO;
import blogsystem.entity.Ad;
import blogsystem.vo.Result;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
public interface IAdService extends IService<Ad> {


    boolean add(AdDTO adDto);
    List<AdDTO> getAdList(String adTypeId);

    boolean delete(String adId) throws ClientException;

    Result updateById(AdDTO adDto) throws ClientException;
}
