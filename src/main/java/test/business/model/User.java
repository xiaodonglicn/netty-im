package test.business.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("online")
    private boolean online;

    @TableField("created_at")
    private String createdAt;
}
