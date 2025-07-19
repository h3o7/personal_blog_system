package blogsystem.service;

import blogsystem.dto.LoginDTO;
import blogsystem.vo.PageResult;
import blogsystem.entity.User;
import blogsystem.vo.Result;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author h3o7
 * @since 2025-06-05
 */
public interface IUserService extends IService<User> {

    PageResult getInfo(Integer pageNum, Integer pageSize,String userId, String userName);

    boolean freezeUser(String userId);

    Result loginInfo(LoginDTO loginDTO);

    Result updateUser(User user) throws ClientException;

    Result getCurrentUser(String userId);

    Result followUser(String userId);

    Result isFollowed(String userId);

    Result getLikedArticles(LocalDateTime localDateTime, Integer limit,String userId);

    Result getCollectedArticles(LocalDateTime localDateTime, Integer limit,String userId);

    Result getFollowingUsers(Integer pageNum, Integer pageSize);
    Result getFollowers(Integer pageNum, Integer pageSize);
    Result getMyArticles(Integer pageNum, Integer pageSize);
}
