package im;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MessageType {

    PRIVATE(1),
    GROUP(2),
    ERROR(-1),
    ;

    private Integer type;

    // 创建一个Map来存储枚举值和它们的code
    private static final Map<Integer, MessageType> codeToEnumMap = new HashMap<>();

    // 静态代码块，用于初始化Map
    static {
        for (MessageType enumValue : MessageType.values()) {
            codeToEnumMap.put(enumValue.getType(), enumValue);
        }
    }

    public static MessageType match(Integer code) {
        MessageType type = codeToEnumMap.get(code);
        if (type != null) {
            return type;
        }
        return ERROR;
    }
}
