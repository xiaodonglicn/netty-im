package im;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CommandType {

    // 建立连接
    CONNECTION(10001),
    CHAT(10002),
    JOIN_GROUP(10003),
    CREATE_GROUP(10004),
    ERROR(-1),
    ;

    private Integer code;

    // 创建一个Map来存储枚举值和它们的code
    private static final Map<Integer, CommandType> codeToEnumMap = new HashMap<>();

    // 静态代码块，用于初始化Map
    static {
        for (CommandType enumValue : CommandType.values()) {
            codeToEnumMap.put(enumValue.getCode(), enumValue);
        }
    }

    public static CommandType match(Integer code) {
        CommandType type = codeToEnumMap.get(code);
        if (type != null) {
            return type;
        }
        return ERROR;
    }
}
