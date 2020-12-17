package com.config.webSocket;

/**
 * @author Mrlu
 * @createTime 2020/5/23
 * @describe  WebSocket信息实体类
 */
public class WebSocketMessage {
    private  String to; //发送给谁
    private  String from;//消息来源
    private  String message;//信息

    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public WebSocketMessage() {

    }
    public WebSocketMessage(String to, String from, String message) {
        this.to = to;
        this.from = from;
        this.message = message;
    }

    public String getto() {
        return to;
    }

    public void setto(String to) {
        this.to = to;
    }

    public String getfrom() {
        return from;
    }

    public void setfrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
