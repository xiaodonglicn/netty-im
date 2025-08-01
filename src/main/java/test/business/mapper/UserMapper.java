package test.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import test.business.model.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    List<User> selectAllExcept(String username);
}
