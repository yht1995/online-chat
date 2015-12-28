package cn.yaoht.onlinechat.midware;

import android.util.Log;

import junit.framework.TestCase;

import cn.yaoht.onlinechat.model.Friend;
import cn.yaoht.onlinechat.model.Message;
import io.realm.RealmList;

/**
 * Created by yaoht on 2015/12/26.
 * Project: OnlineChat
 */
public class SerializerTest extends TestCase {

    public void testMessagetoJson() throws Exception {
        Message message = new Message();

        Friend to = new Friend();
        to.setUser_id("2013011515");
        RealmList<Friend> friendRealmList = new RealmList<>();
        friendRealmList.add(to);
        message.setTo_friend(friendRealmList);

        Friend from = new Friend();
        from.setUser_id("2013011521");

        message.setFrom_friend(from);
        message.setSession_uuid("550e8400-e29b-41d4-a716-446655440000");
        message.setType("msg");
        message.setContent("Hello World");

        String json_string = Serializer.MessagetoJson(message);
        Log.i("Json_test",json_string);
    }
}