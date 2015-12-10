package cn.yaoht.onlinechat.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by yaoht on 2015/12/4.
 * Project: OnlineChat
 */
public class Message extends RealmObject {
    private Date created_time;
    private Date recived_time;
    private String msg;

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public Date getRecived_time() {
        return recived_time;
    }

    public void setRecived_time(Date recived_time) {
        this.recived_time = recived_time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
