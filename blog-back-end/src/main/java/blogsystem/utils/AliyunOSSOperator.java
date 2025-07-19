package blogsystem.utils;

import blogsystem.config.AliyunOSSProperties;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;

import static blogsystem.utils.SystemConstants.OSS_PREFIX;

@Component
public class AliyunOSSOperator {
    @Autowired
    private AliyunOSSProperties aliyunOSSProperties;
    public String upload(byte[] content, String originalFileName, UploadType uploadType) throws Exception {
        // 验证上传参数
        validateUploadParams(content, originalFileName);
        // 获取配置参数
        String endpoint = aliyunOSSProperties.getEndpoint();
        String bucketName = aliyunOSSProperties.getBucketName();
        String region = aliyunOSSProperties.getRegion();
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 填写Object完整路径，例如202406/1.png。Object完整路径中不能包含Bucket名称。
        //获取当前系统日期的字符串,格式为 yyyy/MM

        //生成一个新的不重复的文件名
        String newFileName = IdUtil.simpleUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
        // 目录加文件名
        String objectName = OSS_PREFIX + uploadType.getPath() + newFileName;

        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
        } finally {
            ossClient.shutdown();
        }
        return endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + objectName;
    }
    
    public String deleteFile(String fileUrl) throws ClientException {
        String endpoint = aliyunOSSProperties.getEndpoint();
        String bucketName = aliyunOSSProperties.getBucketName();
        String region = aliyunOSSProperties.getRegion();
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 获取目录名
        String directory = fileUrl.substring(
                fileUrl.indexOf("/", fileUrl.indexOf(bucketName)) + 1,
                fileUrl.lastIndexOf("/")
        );
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        String objectName = directory + "/" + fileName;

        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
        try {
            ossClient.deleteObject(bucketName, objectName);
            return "删除成功";
        } catch (Exception e) {
            return "删除失败: " + e.getMessage();
        } finally {
            ossClient.shutdown();
        }
    }

    private void validateUploadParams(byte[] content, String originalFilename) {
        if (content == null || content.length == 0) {
            throw new IllegalArgumentException("文件内容不能为空");
        }
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("文件名格式不正确");
        }
    }

}