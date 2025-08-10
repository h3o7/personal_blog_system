package blogsystem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Schema(description = "分页响应结果")
public class PageResult {
//    @Schema(description = "总记录数", example = "100")
    private Long total; //总记录数
//    @Schema(description = "当前页数据列表")
    private List rows; //当前页数据列表
}
