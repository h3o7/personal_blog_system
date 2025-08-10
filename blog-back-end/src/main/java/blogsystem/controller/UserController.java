package blogsystem.controller;


import blogsystem.vo.PageResult;
import blogsystem.vo.Result;
import blogsystem.entity.User;
import com.aliyuncs.exceptions.ClientException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import blogsystem.service.IUserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "用户接口", description = "提供用户的增删改查等操作")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;



    // 获取当前登录用户信息
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/current")
    public Result getCurrentUser(@RequestParam String userId) {
        return userService.getCurrentUser(userId);
    }

    // 获取登录用户喜欢的文章列表
    @Operation(summary = "获取用户喜欢的文章列表", description = "根据用户ID获取用户喜欢的文章列表")
    @GetMapping("/liked")
    public Result getLikedArticles(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime addTime,
                                   @RequestParam Integer limit,
                                   @RequestParam String userId) {
        ZoneId utcZone = ZoneId.of("UTC");  // UTC时区
        ZoneId localZone = ZoneId.systemDefault();  // 或指定为 "Asia/Shanghai"

        ZonedDateTime utcDateTime = addTime.atZone(utcZone);
        LocalDateTime localDateTime = utcDateTime.withZoneSameInstant(localZone).toLocalDateTime();
        return userService.getLikedArticles(localDateTime, limit,userId);
    }

    // 获取登录用户收藏的文章列表
    @Operation(summary = "获取用户收藏的文章列表", description = "根据用户ID获取用户收藏的文章列表")
    @GetMapping("/collected")
    public Result getCollectedArticles(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime addTime,
                                       @RequestParam Integer limit,
                                       @RequestParam String userId) {
        ZoneId utcZone = ZoneId.of("UTC");  // UTC时区
        ZoneId localZone = ZoneId.systemDefault();  // 或指定为 "Asia/Shanghai"

        ZonedDateTime utcDateTime = addTime.atZone(utcZone);
        LocalDateTime localDateTime = utcDateTime.withZoneSameInstant(localZone).toLocalDateTime();
        return userService.getCollectedArticles(localDateTime, limit,userId);
    }

    @Operation(summary = "关注用户", description = "关注其他用户")
    @PutMapping("/follow")
    public Result followUser(@RequestParam String userId) {
        return userService.followUser(userId);
    }

    @GetMapping("/isFollow")
    @Operation(summary = "检查是否关注用户", description = "检查当前用户是否已关注指定用户")
    public Result isFollowed(@RequestParam String userId) {
        return userService.isFollowed(userId);
    }

    @GetMapping("/following")
    @Operation(summary = "获取用户关注列表", description = "获取当前用户关注的用户列表")
    public Result following(@RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.getFollowingUsers(pageNum,pageSize);
    }

    @GetMapping("/followers")
    @Operation(summary = "获取用户粉丝列表", description = "获取当前用户的粉丝列表")
    public Result followers(@RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.getFollowers(pageNum,pageSize);
    }


    @GetMapping("/myArticles")
    @Operation(summary = "获取用户的文章列表", description = "根据用户ID获取用户发布的文章列表")
    public Result myArticles(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.getMyArticles(pageNum, pageSize);
    }



    // 管理员的相关操作（非客户端接口）
    @Operation(summary = "获取用户信息", description = "分页查询用户信息，支持根据用户ID和用户名进行过滤")
    @GetMapping("/list")
    public Result getInfo(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize
            ,@RequestParam String userId, @RequestParam String userName) {
        PageResult page = userService.getInfo(pageNum,pageSize,userId, userName);
        return Result.success(page);
    }

    // 根据id删除
    @Operation(summary = "删除用户", description = "根据用户ID删除指定用户")
    @DeleteMapping
    public Result deleteById(@RequestParam String userId) {
        boolean result = userService.removeById(userId);
        if (result) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败，用户不存在或已被删除");
        }
    }

    // 根据id冻结用户
    @Operation(summary = "冻结用户", description = "根据用户ID冻结指定用户")
    @PutMapping("/freeze")
    public Result freezeUser(@RequestParam String userId) {
        boolean result = userService.freezeUser(userId);
        if (result) {
            return Result.success("用户已被冻结");
        } else {
            return Result.error("冻结失败，用户不存在或已被冻结");
        }
    }

    // 对用户信息进行编辑操作
    @Operation(summary = "编辑用户信息", description = "根据用户ID更新用户信息")
    @PutMapping("/edit")
    public Result editUser(@RequestBody User user) throws ClientException {
        return userService.updateUser(user);
    }

    // 新增用户
    @Operation(summary = "添加新用户", description = "添加新的用户信息")
    @PostMapping("/add")
    public Result addUser(@RequestBody User user) {
        user.setUserRegisterTime(LocalDateTime.now());
        boolean result = userService.save(user);
        log.info("新增用户: {}", user);
        if (result) {
            return Result.success("用户已添加");
        } else {
            return Result.error("添加失败，用户可能已存在");
        }
    }



}
