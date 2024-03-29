package cn.yaoht.onlinechat.midware;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Objects;

import cn.yaoht.onlinechat.backend.CoreService;
import cn.yaoht.onlinechat.model.Friend;
import cn.yaoht.onlinechat.model.Message;
import cn.yaoht.onlinechat.model.Session;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by yaoht on 2015/12/27.
 * Project: OnlineChat
 */

/**
 * @author yaoht
 *         消息中间件
 *         <p>
 *         处理收发消息的数据库操作
 *         </p>
 */
public class MessageMidware {

    public static final String TYPE_MSG = "msg";
    public static final String TYPE_FILE = "file";

    static private Realm realm = Realm.getDefaultInstance();
    static private ServerMidware serverMidware = ServerMidware.getInstance();

    public static Friend getFriend(String user_id) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Friend> friendRealmResults = realm.where(Friend.class).equalTo("user_id", user_id).findAll();
        Friend friend;
        if (friendRealmResults.size() == 0) {
            friend = realm.createObject(Friend.class);
            friend.setUser_id(user_id);
            friend.setIp_address("");
            friend.setOn_line(false);
            friend.setNick_name("");
        } else {
            friend = friendRealmResults.first();
        }
        return friend;
    }

    /**
     * 发送消息
     *
     * @param session 会话
     * @param msg     消息内容
     * @param type    消息类型
     */
    public void SendMessage(Session session, String msg, String type) {
        realm.beginTransaction();
        Friend self = getFriend(serverMidware.getUsername());

        Message message = realm.createObject(Message.class);
        message.setSession_uuid(session.getUuid());
        message.setFrom_friend(self);
        message.setTo_friend(session.getFriends());
        message.setType(type);
        message.setContent(msg);
        message.setReceived_time(new Date());
        if (Objects.equals(message.getType(), TYPE_MSG)) {
            session.setMessages(message.getContent());
        } else if (Objects.equals(message.getType(), TYPE_FILE)) {
            session.setMessages("[File]");
        }
        session.setUpdate_time(new Date());
        realm.commitTransaction();

        RawMessage rawMessage = new RawMessage(message);

        new SendMessageAsync().execute(rawMessage);
    }

    /**
     * 接收消息
     *
     * @param rawMessage 解码的信息
     * @return 数据库中保存的消息对象
     */
    public Message ReceiveMessage(RawMessage rawMessage) {
        final Message message = new Message();
        message.setReceived_time(new Date());
        message.setSession_uuid(rawMessage.session_uuid);

        realm.beginTransaction();
        message.setFrom_friend(getFriend(rawMessage.from_friend));

        final RealmList<Friend> to_friend_list = new RealmList<>();
        ServerMidware serverMidware = ServerMidware.getInstance();
        for (int i = 0; i < rawMessage.to_friend.size(); i++) {
            String user_id = rawMessage.to_friend.get(i);
            if (!Objects.equals(user_id, serverMidware.getUsername())) {
                to_friend_list.add(getFriend(user_id));
            }
        }
        message.setTo_friend(to_friend_list);

        message.setType(rawMessage.type);
        message.setContent(rawMessage.content);

        Session session;
        realm.copyToRealm(message);
        RealmResults<Session> sessionRealmResults = realm.where(Session.class).equalTo("uuid", message.getSession_uuid()).findAll();
        if (sessionRealmResults.size() == 0) {
            session = realm.createObject(Session.class);
            session.setUuid(message.getSession_uuid());
            RealmList<Friend> friends = new RealmList<>();
            for (Friend f : message.getTo_friend()) {
                if (!Objects.equals(serverMidware.getUsername(), f.getUser_id())) {
                    friends.add(f);
                }
            }
            friends.add(message.getFrom_friend());
            session.setFriends(friends);
        } else {
            session = sessionRealmResults.first();
        }
        session.setUpdate_time(new Date());
        if (Objects.equals(message.getType(), TYPE_MSG)) {
            session.setMessages(message.getContent());
        } else if (Objects.equals(message.getType(), TYPE_FILE)) {
            session.setMessages("[File]");
        }
        realm.commitTransaction();

        return message;
    }

    /**
     * @author yaoht
     * @see android.os.AsyncTask
     * 异步任务，发送信息
     */
    private class SendMessageAsync extends AsyncTask<RawMessage, Void, Void> {
        @Override
        protected Void doInBackground(RawMessage... params) {
            RawMessage rawMessage = params[0];
            String msg = "";
            try {
                msg = Serializer.MessagetoJson(rawMessage);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            for (String ip : rawMessage.to_ip_list) {
                try {
                    Socket socket = new Socket(ip, CoreService.P2P_PORT);
                    PrintWriter out = new PrintWriter(
                            new BufferedWriter(new OutputStreamWriter(
                                    socket.getOutputStream())), true);
                    out.print(msg);
                    out.flush();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
