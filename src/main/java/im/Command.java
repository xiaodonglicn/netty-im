package im;

import lombok.Data;

/**
 * @Author: Yeast
 * @Date: 2024/02/28 2:38 PM
 */
@Data
public class Command {

    /**
     * 连接信息编码
     */
    private Integer code;

    /**
     * 昵称
     */
    private String nickname;
}
