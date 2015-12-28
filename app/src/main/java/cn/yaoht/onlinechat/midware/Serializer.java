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

    public static final String FIELD_UUID = "UUID";
    public static final String FIELD_FROM = "from";
    public static final String FIELD_TO = "to";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_NAME = "name";
    public static final String TYPE_MSG = "msg";
    public static final String TYPE_FILE = "file";

    public static String MessagetoJson(RawMessage message) throws JSONException, IOException {
        JSONObject json_message = new JSONObject();
        JSONArray json_array_to_friend = new JSONArray();

        for (int i = 0; i < message.to_friend.size(); i++) {
            json_array_to_friend.put(message.to_friend.get(i));
        }
        json_message.put(FIELD_UUID, message.session_uuid);
        json_message.put(FIELD_FROM, message.from_friend);
        json_message.put(FIELD_TO, json_array_to_friend);
        json_message.put(FIELD_TYPE, message.type);

        if (Objects.equals(message.type, TYPE_MSG)) {
            json_message.put(FIELD_CONTENT, message.content);
        } else if (Objects.equals(message.type, TYPE_FILE)) {
            File file = new File(message.content);
            String encodedBase64 = FileEncodeBase64(file);
            json_message.put(FIELD_NAME, file.getName());
            json_message.put(FIELD_CONTENT, encodedBase64);
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

        rawMessage.session_uuid = json.getString(FIELD_UUID);
        rawMessage.from_friend = json.getString(FIELD_FROM);

        JSONArray to_friend = json.getJSONArray(FIELD_TO);
        for (int i = 0; i < to_friend.length(); i++) {
            rawMessage.to_friend.add((String) (to_friend.get(i)));
        }

        rawMessage.type = json.getString(FIELD_TYPE);

        if (Objects.equals(rawMessage.type, TYPE_MSG)) {
            rawMessage.content = json.getString(FIELD_CONTENT);
        } else if (Objects.equals(rawMessage.type, TYPE_FILE)) {
            rawMessage.content = FileDecodeBase64(json.getString(FIELD_CONTENT), json.getString(FIELD_NAME));
        }
        return rawMessage;
    }
}
