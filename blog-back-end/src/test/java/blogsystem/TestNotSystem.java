package blogsystem;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;

import java.time.LocalDateTime;
import java.util.Date;

public class TestNotSystem {
    public static void main(String[] args) {
        String bucketName = "y-37";
        String adImageUrl = "https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/ad/ee194fde8cc816cf79350ff698c4ab79.jpg";
        String fileName = adImageUrl.substring(adImageUrl.lastIndexOf("/") + 1);
        // 通过fileName获取blog-system/ad
        // 目录名
        String directory = adImageUrl.substring(
            adImageUrl.indexOf("/", adImageUrl.indexOf(bucketName)) + 1,
            adImageUrl.lastIndexOf("/")
        );

        // 获取目录
        Console.log(directory);
        Console.log(fileName);
    }
}

