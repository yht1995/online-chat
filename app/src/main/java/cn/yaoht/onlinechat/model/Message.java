package cn.yaoht.onlinechat.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by yaoht on 2015/12/4.
 * Project: OnlineChat
 */

public class Message extends RealmObject {

    @Index
    private String session_uuid;

    private Friend from_friend;
    private RealmList<Friend> to_friend;
    private String type;
    private String content;
    private Date received_time;

    public Friend getFrom_friend() {
        return from_friend;
    }

    public void setFrom_friend(Friend from_friend) {
        this.from_friend = from_friend;
    }

    public RealmList<Friend> getTo_friend() {
        return to_friend;
    }

    public void setTo_friend(RealmList<Friend> to_friend) {
        this.to_friend = to_friend;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getReceived_time() {
        return received_time;
    }

    public void setReceived_time(Date received_time) {
        this.received_time = received_time;
    }

    public String getSession_uuid() {
        return session_uuid;
    }

    public void setSession_uuid(String session_uuid) {
        this.session_uuid = session_uuid;
    }
}

