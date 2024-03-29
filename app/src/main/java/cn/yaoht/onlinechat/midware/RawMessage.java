package cn.yaoht.onlinechat.midware;

import java.util.ArrayList;
import java.util.Objects;

import cn.yaoht.onlinechat.model.Friend;
import cn.yaoht.onlinechat.model.Message;

/**
 * Created by yaoht on 2015/12/28.
 * Project: OnlineChat
 */

/**
 * @author yaoht
 *         用于发送和接收的原始消息结构
 */

public class RawMessage {

    public static final String TYPE_MSG = "msg";
    public static final String TYPE_FILE = "file";

    public String session_uuid;
    public String from_friend;
    public ArrayList<String> to_friend;
    public ArrayList<String> to_ip_list;
    public String type;
    public String content;

    public RawMessage() {
        to_friend = new ArrayList<>();
        to_ip_list = new ArrayList<>();
    }

    public RawMessage(Message message) {
        session_uuid = message.getSession_uuid();
        from_friend = message.getFrom_friend().getUser_id();
        to_friend = new ArrayList<>();
        to_ip_list = new ArrayList<>();
        for (Friend friend : message.getTo_friend()) {
            to_friend.add(friend.getUser_id());
            to_ip_list.add(friend.getIp_address());
        }
        type = message.getType();
        content = message.getContent();
    }

    /**
     * 获取消息内容描述
     *
     * @return 消息内容描述
     */
    public String getContentDescription() {
        if (Objects.equals(type, TYPE_MSG)) {
            return content;
        }
        return "[File]";
    }
}
