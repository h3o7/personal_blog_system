package blogsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("blogsystem.mapper") // 扫描 Mapper 接口
public class BlogSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogSystemApplication.class, args);
    }

}
