package cn.yaoht.onlinechat.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.Objects;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.midware.ServerMidware;
import cn.yaoht.onlinechat.model.Message;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by yaoht on 2015/12/26.
 * Project: OnlineChat
 */
public class MessageRecyclerViewAdapter extends RealmBaseAdapter<Message> implements ListAdapter {

    private final int MY_MESSAGE = 0, OTHER_MESSAGE = 1;

    public MessageRecyclerViewAdapter(Context context, RealmResults<Message> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = realmResults.get(position);
        ServerMidware serverMidware = ServerMidware.getInstance();
        if (Objects.equals(message.getFrom_friend().getUser_id(), serverMidware.getUsername())) {
            return MY_MESSAGE;
        }
        return OTHER_MESSAGE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewtype = getItemViewType(position);
        ViewHolder viewHolder = new ViewHolder();
        Message message = realmResults.get(position);
        switch (viewtype) {
            case MY_MESSAGE:
                convertView = inflater.inflate(R.layout.activity_message_mine_item, parent, false);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.message_mine_image);
                viewHolder.username = (TextView) convertView.findViewById(R.id.message_mine_text_username);
                viewHolder.massage = (TextView) convertView.findViewById(R.id.message_mine_text_message);
                break;
            case OTHER_MESSAGE:
                convertView = inflater.inflate(R.layout.activity_message_other_item, parent, false);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.message_other_image);
                viewHolder.username = (TextView) convertView.findViewById(R.id.message_other_text_username);
                viewHolder.massage = (TextView) convertView.findViewById(R.id.message_other_text_message);
        }
        viewHolder.massage.setText(message.getContent());
        viewHolder.username.setText(message.getFrom_friend().getUser_id());
        return convertView;
    }

    private static class ViewHolder {
        TextView username;
        ImageView image;
        TextView massage;
    }
}
