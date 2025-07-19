package blogsystem.service.impl;

import blogsystem.dto.AdDTO;
import blogsystem.entity.Ad;
import blogsystem.mapper.AdMapper;
import blogsystem.service.IAdService;
import blogsystem.utils.AliyunOSSOperator;
import blogsystem.vo.Result;
import cn.hutool.core.util.IdUtil;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements IAdService {
    @Autowired
    private AdMapper adMapper;
    @Autowired
    AliyunOSSOperator aliyunOSSOperator;
    @Override
    public boolean add(AdDTO adDto) {
        Ad ad = new Ad();
        adDto.setAdId(IdUtil.simpleUUID());
        BeanUtils.copyProperties(adDto, ad);
        ad.setAdAddTime(LocalDateTime.now());
        adMapper.insert(ad);
        return true;
    }

    @Override
    public List<AdDTO> getAdList(String adTypeId) {
        List<AdDTO> adList = adMapper.getAdList(adTypeId);
        if (adList != null && !adList.isEmpty()) {
            return adList;
        }
        return List.of();
    }

    @Override
    public boolean delete(String adId) throws ClientException {
        // 根据广告ID查询广告图片地址
        Ad ad = adMapper.selectById(adId);
        String adImageUrl = ad.getAdImgUrl();
        // 获取文件名
        String isDeleted = aliyunOSSOperator.deleteFile(adImageUrl);
        if (isDeleted.equals("删除成功")) {
            // 删除数据库中的广告记录
            int result = adMapper.deleteById(adId);
            return result > 0;
        }
        return false;
    }

    @Override
    public Result updateById(AdDTO adDto) throws ClientException {
        Ad ad = new Ad();
        String oldAdImageUrl = adMapper.selectById(adDto.getAdId()).getAdImgUrl();
        // 如果广告图片地址不为空，删除旧的图片
        if (oldAdImageUrl != null && !oldAdImageUrl.isEmpty()) {
            if(! oldAdImageUrl.equals(adDto.getAdImgUrl())){
                aliyunOSSOperator.deleteFile(oldAdImageUrl);
            }

        }
        BeanUtils.copyProperties(adDto, ad);
        int flag = adMapper.updateById(ad);
        // 更新成功后返回结果
        if (flag > 0) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
}
