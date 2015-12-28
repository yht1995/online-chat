package cn.yaoht.onlinechat.midware;

import android.os.Environment;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by yaoht on 2015/12/26.
 * Project: OnlineChat
 */
public class Serializer {

    public static String MessagetoJson(RawMessage message) throws JSONException, IOException {
        JSONObject json_message = new JSONObject();
        JSONArray json_array_to_friend = new JSONArray();

        for (int i = 0; i < message.to_friend.size(); i++) {
            json_array_to_friend.put(message.to_friend.get(i));
        }
        json_message.put("UUID", message.session_uuid);
        json_message.put("from", message.from_friend);
        json_message.put("to", json_array_to_friend);
        json_message.put("type", message.type);

        if (Objects.equals(message.type, "msg")) {
            json_message.put("content", message.content);
        } else if (Objects.equals(message.type, "file")) {
            File file = new File(message.content);
            String encodedBase64 = FileEncodeBase64(file);
            json_message.put("name", file.getName());
            json_message.put("content", encodedBase64);
        }
        return json_message.toString();
    }

    private static String FileEncodeBase64(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        stream.read(bytes);
        stream.close();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private static String FileDecodeBase64(String base64, String filename) throws IOException {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), filename);

        FileOutputStream stream = new FileOutputStream(file);
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        stream.write(bytes);
        stream.flush();
        stream.close();
        return file.getAbsolutePath();
    }

    public static RawMessage JsontoMessage(String str) throws JSONException, IOException {
        JSONObject json = new JSONObject(str);
        RawMessage rawMessage = new RawMessage();

        rawMessage.session_uuid = json.getString("UUID");
        rawMessage.from_friend = json.getString("from");

        JSONArray to_friend = json.getJSONArray("to");
        for (int i = 0; i < to_friend.length(); i++) {
            rawMessage.to_friend.add((String) (to_friend.get(i)));
        }

        rawMessage.type = json.getString("type");

        if (Objects.equals(rawMessage.type, "msg")) {
            rawMessage.content = json.getString("content");
        } else if (Objects.equals(rawMessage.type, "file")) {
            rawMessage.content = FileDecodeBase64(json.getString("content"), json.getString("name"));
        }
        return rawMessage;
    }
}
