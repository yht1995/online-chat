package cn.yaoht.onlinechat.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.midware.MessageMidware;
import cn.yaoht.onlinechat.model.Friend;
import cn.yaoht.onlinechat.model.Message;
import cn.yaoht.onlinechat.model.Session;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MessageActivity extends AppCompatActivity {

    public final static String SESSION_UUID = "cn.yaoht.onlinechat.SESSION_UUID";
    public final static String SESSION_FRIEND = "cn.yaoht.onlinechat.FRIEND";

    public final static int SELECT_FILE_REQUSET = 1;

    private Realm realm;
    private Session session;
    private RealmResults<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        if (intent.hasExtra(SESSION_UUID)) {
            final String session_uuid = intent.getStringExtra(SESSION_UUID);
            CreateFromSession(session_uuid);
        } else if (intent.hasExtra(SESSION_FRIEND)) {
            final ArrayList<String> friend_list = intent.getStringArrayListExtra(SESSION_FRIEND);
            CreateFromFriend(friend_list);
        }

        MessageRecyclerViewAdapter messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(this, messages, true);
        final ListView listView = (ListView) findViewById(R.id.activity_message_list_view);
        listView.setAdapter(messageRecyclerViewAdapter);

        final EditText editText = (EditText) findViewById(R.id.activity_message_edittext_message);


        Button button_send = (Button) findViewById(R.id.activity_message_button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                if (Pattern.matches("^\\s*$", msg)) {
                    Snackbar.make(listView, "Do not send blank text", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                editText.setText("");
                MessageMidware messageMidware = new MessageMidware();
                messageMidware.SendMessage(session, msg, "msg");
            }
        });

        Button button_file = (Button) findViewById(R.id.activity_message_button_file);
        button_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("File/*");
                startActivityForResult(intent, SELECT_FILE_REQUSET);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE_REQUSET && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            MessageMidware messageMidware = new MessageMidware();
            messageMidware.SendMessage(session, uri.getPath(), "file");
        }
    }

    private void CreateFromSession(String session_uuid) {
        session = realm.where(Session.class).equalTo("uuid", session_uuid).findFirst();
        messages = realm.where(Message.class).equalTo("session_uuid", session_uuid).findAll();
        messages.sort("received_time");
    }

    private void CreateFromFriend(ArrayList<String> friend_list) {
        realm.beginTransaction();
        session = realm.createObject(Session.class);
        session.setUuid(UUID.randomUUID().toString());
        RealmList<Friend> friendRealmList = new RealmList<>();
        for (String friend_id : friend_list) {
            friendRealmList.add(MessageMidware.getFriend(friend_id));
        }
        session.setFriends(friendRealmList);
        session.setMessages("");
        session.setUpdate_time(new Date());
        realm.commitTransaction();
        CreateFromSession(session.getUuid());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
