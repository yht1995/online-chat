package cn.yaoht.onlinechat.midware;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import cn.yaoht.onlinechat.P2PIntentService;
import cn.yaoht.onlinechat.Utility;
import cn.yaoht.onlinechat.model.Friend;
import cn.yaoht.onlinechat.model.Message;
import cn.yaoht.onlinechat.model.Session;
import io.realm.Realm;

/**
 * Created by yaoht on 2015/12/27.
 * Project: OnlineChat
 */
public class MessageMidware {

    static private Realm realm = Realm.getDefaultInstance();
    static private ServerMidware serverMidware = ServerMidware.getInstance();

    public void SendMessage(Session session, String msg, String type) {
        realm.beginTransaction();
        Friend self = Serializer.getFriend(serverMidware.getUsername());
        self.setUser_id(serverMidware.getUsername());
        Message message = realm.createObject(Message.class);
        message.setSession_uuid(session.getUuid());
        message.setFrom_friend(self);
        message.setTo_friend(session.getFriends());
        message.setType(type);
        message.setContent(msg);
        message.setReceived_time(new Date());
        session.setMessages(msg);
        session.setUpdate_time(new Date());
        realm.commitTransaction();
        SendMessage sendMessage = new SendMessage();
        sendMessage.ip_address = new ArrayList<>();
        for (Friend friend : message.getTo_friend()) {
            if (Utility.CheckIPAddress(friend.getIp_address())) {
                sendMessage.ip_address.add(friend.getIp_address());
            }
        }
        try {
            sendMessage.msg = Serializer.MessagetoJson(message);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        new SendMessageAsync().execute(sendMessage);
    }

    private class SendMessage {
        public ArrayList<String> ip_address;
        public String msg;
    }

    private class SendMessageAsync extends AsyncTask<SendMessage, Void, Void> {
        @Override
        protected Void doInBackground(SendMessage... params) {
            SendMessage sendMessage = params[0];
            for (String ip : sendMessage.ip_address) {
                try {
                    Socket socket = new Socket(ip, P2PIntentService.P2P_PORT);
                    PrintWriter out = new PrintWriter(
                            new BufferedWriter(new OutputStreamWriter(
                                    socket.getOutputStream())), true);
                    out.print(sendMessage.msg);
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
