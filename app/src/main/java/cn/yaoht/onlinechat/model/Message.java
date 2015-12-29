package cn.yaoht.onlinechat.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by yaoht on 2015/12/4.
 * Project: OnlineChat
 */

/**
 * @author yaoht
 * @link https://realm.io/cn/docs/java/latest/#section-4
 * <p>
 * 数据库模型：消息
 * RealmObject子类的getter和setter会被生成的代理类重载，添加到getter和setter的任何自定义逻辑实际上并不会被执行。
 */
public class Message extends RealmObject {

    /**
     * 索引
     *
     * @link https://realm.io/cn/docs/java/latest/#index
     * 会话ID
     */
    @Index
    private String session_uuid;

    /**
     * 发送方
     */
    private Friend from_friend;

    /**
     * 接收方列表
     */
    private RealmList<Friend> to_friend;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息类型
     */
    private String content;

    /**
     * 接收时间
     */
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

