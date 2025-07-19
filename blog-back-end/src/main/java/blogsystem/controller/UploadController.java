package blogsystem.controller;


import blogsystem.entity.User;
import blogsystem.utils.UploadType;
import blogsystem.vo.Result;
import blogsystem.utils.AliyunOSSOperator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/upload")
@Tag(name = "文件上传接口", description = "提供文件上传相关操作接口")
public class UploadController {
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    @PostMapping("/ad")
    public Result uploadAd(MultipartFile file) throws Exception {
        log.info("上传文件：{}",file);
        if(!file.isEmpty()){
            String originalFileName = file.getOriginalFilename();
            // 上传文件
            String url = aliyunOSSOperator.upload(file.getBytes(),
                    originalFileName,
                    UploadType.AD_POSTER
            );
            return Result.success(url);
        }
        return Result.error("上传失败");
    }

    @PostMapping("/user")
    public Result uploadAvatar(MultipartFile file) throws Exception {
        log.info("上传文件：{}", file);
        if (!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            // 上传文件
            String url = aliyunOSSOperator.upload(file.getBytes(),
                    originalFileName,
                    UploadType.USER_AVATAR
            );
            return Result.success(url);
        }
        return Result.error("上传失败");
    }

}
