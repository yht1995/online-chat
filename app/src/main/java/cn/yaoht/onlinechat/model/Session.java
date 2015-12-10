package cn.yaoht.onlinechat.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by yaoht on 2015/12/4.
 * Project: OnlineChat
 */
public class Session extends RealmObject{
    private RealmList<Friend> friends;
    private RealmList<Message> msg;
    private Date update_time;

    public RealmList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(RealmList<Friend> friends) {
        this.friends = friends;
    }

    public RealmList<Message> getMsg() {
        return msg;
    }

    public void setMsg(RealmList<Message> msg) {
        this.msg = msg;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
