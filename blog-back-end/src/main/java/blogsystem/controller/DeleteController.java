package blogsystem.controller;

import blogsystem.vo.Result;
import blogsystem.utils.AliyunOSSOperator;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "文件删除接口", description = "提供文件删除相关操作接口")
@RequiredArgsConstructor
public class DeleteController {
    private final AliyunOSSOperator aliyunOSSOperator;
    @Operation(summary = "删除文件", description = "根据文件名删除指定文件")
    @PostMapping("/delete")
    public Result delete(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        try {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                return Result.error("文件名不能为空");
            }
            // 删除文件
            aliyunOSSOperator.deleteFile(originalFileName);
            return Result.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败: " + e.getMessage());
        }
    }
}
