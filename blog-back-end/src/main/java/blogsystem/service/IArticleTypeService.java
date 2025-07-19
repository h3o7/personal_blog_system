package blogsystem.service;

import blogsystem.dto.ArticleTypeDTO;
import blogsystem.entity.ArticleType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface IArticleTypeService extends IService<ArticleType> {

    List<ArticleTypeDTO> getAll();
}
