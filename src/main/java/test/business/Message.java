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
    private String timestamp;

    public Message(MessageType type, String username, String content, String timestamp) {
        this.type = type;
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
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
