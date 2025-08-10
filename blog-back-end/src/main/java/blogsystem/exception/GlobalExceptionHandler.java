package blogsystem.exception;

import blogsystem.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 标记为全局异常处理器
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {



    // 捕获sql语句的异常信息
    @ExceptionHandler
    public Result sqlException(DuplicateKeyException e) {
        log.error("程序出错",e);
//        e.printStackTrace();
        // 获取异常的数据
        String message = e.getMessage();
        /*
            indexOf("Duplicate entry")：获取字符串中"Duplicate entry"的索引
         */
        int i = message.indexOf("Duplicate entry");
        String substring = message.substring(i);
        String s = substring.split(" ")[2];

        return Result.error("对不起," + s + "已存在,请重新输入");
    }

    // 拦截所有异常，Exception是所有异常的父类
    @ExceptionHandler
    public Result handleException(Exception e) {
        // 打印异常日志，便于排查
        log.error("全局异常捕获：", e);
        // 返回统一的错误响应，包含异常信息
        return Result.error("服务器异常：" + e.getMessage());
    }

}
