package blogsystem.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "统一响应结果")
public class Result {
    @Schema(description = "响应码", example = "200")
    private Integer code;
    @Schema(description = "响应消息", example = "操作成功")
    private String msg;
    @Schema(description = "响应数据")
    private Object data = null;


    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("success");
        return result;
    }



    public static Result success(Object data) {
        Result result = success();
        result.setData(data);
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(404);
        result.setMsg(msg);

        return result;
    }
}

