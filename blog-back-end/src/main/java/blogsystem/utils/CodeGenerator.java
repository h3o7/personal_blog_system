package blogsystem.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.model.ClassAnnotationAttributes;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/blog_system", "root", "20050429000@Zh")
                .globalConfig(builder -> {
                    builder.author("h3o7") // 设置作者
                            .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出目录（绝对路径）
                })
                .packageConfig(builder -> {
                    builder.parent("blogsystem") // 设置父包名（生成到 src/main/java/blogsystem 下）
                            .entity("entity") // 设置实体类包名
                            .mapper("mapper") // 设置 Mapper 接口包名
                            .service("service") // 设置 Service 接口包名
                            .serviceImpl("service.impl") // 设置 Service 实现类包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder()
                            .enableFileOverride()
                            .enableSerialAnnotation()
                            .enableLombok(new ClassAnnotationAttributes("@Data","lombok.Data"))
                            .enableLombok(new ClassAnnotationAttributes("@AllArgsConstructor","lombok.AllArgsConstructor"))
                            .enableLombok(new ClassAnnotationAttributes("@NoArgsConstructor","lombok.NoArgsConstructor"));
                    builder.mapperBuilder()
                            .enableMapperAnnotation();
                    builder.controllerBuilder()
                            .enableRestStyle();
                })
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }
}
