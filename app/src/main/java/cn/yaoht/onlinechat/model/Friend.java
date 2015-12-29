package cn.yaoht.onlinechat.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yaoht on 2015/12/4.
 * Project: OnlineChat
 */

/**
 * @author yaoht
 * @link https://realm.io/cn/docs/java/latest/#section-4
 *
 * 数据库模型：好友
 * RealmObject子类的getter和setter会被生成的代理类重载，添加到getter和setter的任何自定义逻辑实际上并不会被执行。
 */
public class Friend extends RealmObject {
    /**
     * 【主键】
     *
     * @link https://realm.io/cn/docs/java/latest/#section-7
     * 用户账号
     */
    @PrimaryKey
    private String user_id;

    /**
     * IP地址
     */
    private String ip_address;

    private String nick_name;

    /**
     * 在线状态
     */
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
