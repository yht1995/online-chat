package cn.yaoht.onlinechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.fragment.SessionRecyclerViewAdapter;
import cn.yaoht.onlinechat.midware.MessageMidware;
import cn.yaoht.onlinechat.model.Message;
import cn.yaoht.onlinechat.model.Session;
import io.realm.Realm;
import io.realm.RealmResults;

public class MessageActivity extends AppCompatActivity {

    private Realm realm;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        realm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        final String session_uuid = intent.getStringExtra(SessionRecyclerViewAdapter.SESSION_UUID);
        session = realm.where(Session.class).equalTo("uuid", session_uuid).findFirst();
        RealmResults<Message> messages = realm.where(Message.class).equalTo("session_uuid", session_uuid).findAll();
        messages.sort("received_time");

        MessageRecyclerViewAdapter messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(this, messages, true);
        ListView listView = (ListView) findViewById(R.id.activity_message_list_view);
        listView.setAdapter(messageRecyclerViewAdapter);

        final EditText editText = (EditText) findViewById(R.id.activity_message_edittext_message);


        Button button_send = (Button) findViewById(R.id.activity_message_button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                editText.setText("");
                MessageMidware messageMidware = new MessageMidware();
                messageMidware.SendMessage(session, msg);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }
}
