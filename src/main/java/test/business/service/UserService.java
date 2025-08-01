package test.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import test.business.mapper.UserMapper;
import test.business.model.User;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;


    public boolean register(String username, String password) {
        if (userMapper.selectById(username) != null) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setOnline(false);
        return userMapper.insert(user) > 0;
    }

    public boolean login(String username, String password) {
//        User user = userMapper.selectById(username);
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, username)
                .last(" limit 1"));
        if (user != null && password.equals(user.getPassword())) {
            user.setOnline(true);
            userMapper.updateById(user);
            return true;
        }
        return false;
    }

    public void logout(String username) {
        User user = userMapper.selectById(username);
        if (user != null) {
            user.setOnline(false);
            userMapper.updateById(user);
        }
    }

    public List<User> getAllUsersExcept(String username) {
        return userMapper.selectAllExcept(username);
    }

    public boolean isUsernameExists(String username) {
        return userMapper.selectById(username) != null;
    }
}
