package blogsystem.mapper;

import blogsystem.dto.ArticleTypeDTO;
import blogsystem.entity.ArticleType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleTypeMapper extends BaseMapper<ArticleType> {
    List<ArticleTypeDTO> getAll();

}
