package blogsystem.service.impl;

import blogsystem.entity.Link;
import blogsystem.mapper.LinkMapper;
import blogsystem.service.ILinkService;
import cn.hutool.core.util.IdUtil;
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
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {
    @Autowired
    private LinkMapper linkMapper;

    @Override
    public boolean add(Link link) {
        link.setLinkId(IdUtil.simpleUUID());
        link.setLinkAddTime(LocalDateTime.now());
        linkMapper.insert(link);
        return true;
    }
}
