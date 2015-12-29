package cn.yaoht.onlinechat.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yaoht on 2015/12/4.
 * Project: OnlineChat
 */

/**
 * @author yaoht
 * @link https://realm.io/cn/docs/java/latest/#section-4
 * <p>
 * 数据库模型：会话
 * RealmObject子类的getter和setter会被生成的代理类重载，添加到getter和setter的任何自定义逻辑实际上并不会被执行。
 */
public class Session extends RealmObject {
    /**
     * 【主键】
     *
     * @link https://realm.io/cn/docs/java/latest/#section-7
     * 会话全局ID
     */
    @PrimaryKey
    private String uuid;

    /**
     * 参与会话的好友（不包括自己）
     */
    private RealmList<Friend> friends;

    /**
     * 最新消息
     */
    private String messages;

    /**
     * 更新时间
     */
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
