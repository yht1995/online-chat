package cn.yaoht.onlinechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.fragment.SessionRecyclerViewAdapter;
import cn.yaoht.onlinechat.model.Message;
import io.realm.Realm;
import io.realm.RealmResults;

public class MessageActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        realm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        String session_uuid = intent.getStringExtra(SessionRecyclerViewAdapter.SESSION_UUID);
        RealmResults<Message> messages = realm.where(Message.class).equalTo("session_uuid", session_uuid).findAll();
        messages.sort("received_time");

        MessageRecyclerViewAdapter messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(this, messages, true);
        ListView listView = (ListView)findViewById(R.id.activity_message_list_view);
        listView.setAdapter(messageRecyclerViewAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }
}
