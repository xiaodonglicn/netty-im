package im;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Yeast
 * @Date: 2024/02/28 3:59 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends Command{

    // 消息类型
    private Integer messageType;
    // 接收者
    private String target;
    // 消息内容
    private String content;
}
