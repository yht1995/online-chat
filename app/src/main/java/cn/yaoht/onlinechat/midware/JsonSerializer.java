package cn.yaoht.onlinechat.midware;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import cn.yaoht.onlinechat.model.Friend;
import cn.yaoht.onlinechat.model.Message;
import cn.yaoht.onlinechat.model.Session;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by yaoht on 2015/12/26.
 * Project: OnlineChat
 */
public class JsonSerializer {

    public static String MessagetoJson(Message message) throws JSONException, IOException {
        JSONObject json_message = new JSONObject();
        JSONArray json_array_to_friend = new JSONArray();

        for (int i = 0; i < message.getTo_friend().size(); i++) {
            json_array_to_friend.put(message.getTo_friend().get(i).getUser_id());
        }

        json_message.put("UUID", message.getSession_uuid());
        json_message.put("from", message.getFrom_friend().getUser_id());
        json_message.put("to", json_array_to_friend);
        json_message.put("type", message.getType());

        if (Objects.equals(message.getType(), "msg")) {
            json_message.put("content", message.getContent());
        } else if (Objects.equals(message.getType(), "file")) {
            File originalFile = new File(message.getContent());
            FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
            byte[] bytes = new byte[(int) originalFile.length()];
            fileInputStreamReader.read(bytes);
            String encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
            json_message.put("name", message.getContent());
            json_message.put("content", encodedBase64);
        }
        return json_message.toString();
    }

    public static Message JsontoMessage(String str) throws JSONException {
        Realm realm = Realm.getDefaultInstance();
        JSONObject json = new JSONObject(str);
        final Message message = new Message();
        message.setReceived_time(new Date());
        message.setSession_uuid(json.getString("UUID"));

        realm.beginTransaction();
        message.setFrom_friend(getFriend(json.getString("from")));

        JSONArray to_friend = json.getJSONArray("to");
        final RealmList<Friend> to_friend_list = new RealmList<>();
        ServerMidware serverMidware = ServerMidware.getInstance();
        for (int i = 0; i < to_friend.length(); i++) {
            String user_id = (String) to_friend.get(i);
            if (!Objects.equals(user_id, serverMidware.getUsername())) {
                to_friend_list.add(getFriend(user_id));
            }
        }
        message.setTo_friend(to_friend_list);

        message.setType(json.getString("type"));
        if (Objects.equals(message.getType(), "msg")) {
            message.setContent(json.getString("content"));
        }


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
        session.setMessages(message.getContent());
        realm.commitTransaction();
        return message;
    }

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
}
