package cn.yaoht.onlinechat.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yaoht on 2015/12/4.
 * Project: OnlineChat
 */
public class Friend extends RealmObject {
    @PrimaryKey
    private String user_id;

    private String ip_address;
    private String nick_name;
    private Boolean on_line;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public Boolean getOn_line() {
        return on_line;
    }

    public void setOn_line(Boolean on_line) {
        this.on_line = on_line;
    }
}
