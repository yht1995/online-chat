package cn.yaoht.onlinechat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.File;
import java.net.URLConnection;
import java.util.Objects;
import java.util.regex.Pattern;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.midware.MessageMidware;
import cn.yaoht.onlinechat.midware.ServerMidware;
import cn.yaoht.onlinechat.model.Message;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import me.himanshusoni.chatmessageview.ChatMessageView;

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
                viewHolder.bubble = (ChatMessageView) convertView.findViewById(R.id.message_mine_bubble);
                break;
            case OTHER_MESSAGE:
                convertView = inflater.inflate(R.layout.activity_message_other_item, parent, false);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.message_other_image);
                viewHolder.username = (TextView) convertView.findViewById(R.id.message_other_text_username);
                viewHolder.massage = (TextView) convertView.findViewById(R.id.message_other_text_message);
                viewHolder.bubble = (ChatMessageView) convertView.findViewById(R.id.message_other_bubble);
        }
        if (Objects.equals(message.getType(), MessageMidware.TYPE_MSG)) {
            viewHolder.massage.setText(message.getContent());
        } else if (Objects.equals(message.getType(), MessageMidware.TYPE_FILE)) {
            viewHolder.image.setVisibility(View.VISIBLE);

            File file = new File(message.getContent());
            String mime = URLConnection.guessContentTypeFromName(file.getName());
            if (mime != null && Pattern.matches("^image/.*", mime)) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 16;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                viewHolder.image.setImageBitmap(bitmap);
                viewHolder.massage.setVisibility(View.GONE);
            } else {
                viewHolder.image.setImageResource(R.drawable.ic_insert_drive_file_48dp);
                viewHolder.massage.setText(new File(message.getContent()).getName());
            }
            viewHolder.bubble.setOnClickListener(new OnItemClickListener(message.getContent()));
        }
        viewHolder.username.setText(message.getFrom_friend().getUser_id());
        return convertView;
    }

    private static class ViewHolder {
        TextView username;
        ImageView image;
        TextView massage;
        ChatMessageView bubble;
    }

    private class OnItemClickListener implements View.OnClickListener {

        private String filepath;

        OnItemClickListener(String filepath) {
            this.filepath = filepath;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File file = new File(filepath);
            String mime = URLConnection.guessContentTypeFromName(file.getName());
            if (mime != null) {
                intent.setDataAndType(Uri.fromFile(file), mime);
                context.startActivity(intent);
            }else{
                Snackbar.make(view, R.string.error_cannot_open, Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
