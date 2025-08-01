package test.business;

public class Message {
    public enum MessageType {
        CHAT,    // 聊天消息
        SYSTEM,  // 系统消息
        JOIN,    // 用户加入
        LEAVE    // 用户离开
    }

    private MessageType type;
    private String username;
    private String content;

    public Message(MessageType type, String username, String content) {
        this.type = type;
        this.username = username;
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }
}
