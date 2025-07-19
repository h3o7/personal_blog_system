package blogsystem.service.impl;

import blogsystem.dto.ArticleTypeDTO;
import blogsystem.mapper.ArticleTypeMapper;
import blogsystem.service.IArticleTypeService;
import blogsystem.entity.ArticleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

@Service
public class ArticleTypeServiceImpl extends ServiceImpl<ArticleTypeMapper, ArticleType> implements IArticleTypeService {

    @Autowired
    private ArticleTypeMapper articleTypeMapper;



    @Override
    public List<ArticleTypeDTO> getAll() {
        List<ArticleTypeDTO> articleTypes = articleTypeMapper.getAll();
        return articleTypes;
    }
}
