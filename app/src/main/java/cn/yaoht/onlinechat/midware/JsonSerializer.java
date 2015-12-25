package cn.yaoht.onlinechat.midware;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import cn.yaoht.onlinechat.model.Message;

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
}
