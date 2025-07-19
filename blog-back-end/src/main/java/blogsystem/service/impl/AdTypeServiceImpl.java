package blogsystem.service.impl;

import blogsystem.entity.Ad;
import blogsystem.entity.AdType;
import blogsystem.mapper.AdMapper;
import blogsystem.mapper.AdTypeMapper;
import blogsystem.service.IAdTypeService;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
@Service
public class AdTypeServiceImpl extends ServiceImpl<AdTypeMapper, AdType> implements IAdTypeService {
    @Autowired
    private AdTypeMapper adTypeMapper;
    @Autowired
    private AdMapper adMapper;
    @Override
    public boolean add(AdType adType) {
        adType.setAdTypeId(IdUtil.simpleUUID());
        adType.setAdTypeAddTime(LocalDateTime.now());
        adTypeMapper.insert(adType);
        return true;
    }

    @Override
    public boolean deleteById(String adTypeId) {
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ad_type_id", adTypeId);
        long count = adMapper.selectCount(queryWrapper);
        if(count > 0){
            return false; // 广告类型正在被使用，不能删除
        }else{
            // 如果没有被使用，则可以删除
            adTypeMapper.deleteById(adTypeId);
            return true; // 删除成功
        }
    }


}
