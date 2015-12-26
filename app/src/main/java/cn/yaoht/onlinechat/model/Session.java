package cn.yaoht.onlinechat.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yaoht on 2015/12/4.
 * Project: OnlineChat
 */
public class Session extends RealmObject {
    @PrimaryKey
    private String uuid;

    private RealmList<Friend> friends;
    private String messages;
    private Date update_time;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public RealmList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(RealmList<Friend> friends) {
        this.friends = friends;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
