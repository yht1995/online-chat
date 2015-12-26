package cn.yaoht.onlinechat.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.activity.MessageActivity;
import cn.yaoht.onlinechat.model.Friend;
import cn.yaoht.onlinechat.model.Session;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;


/**
 * Created by yaoht on 2015/12/26.
 * Project: OnlineChat
 */
public class SessionRecyclerViewAdapter extends RealmBasedRecyclerViewAdapter<Session,
        SessionRecyclerViewAdapter.ViewHolder> {

    public final static String SESSION_UUID = "cn.yaoht.onlinechat.SESSION_UUID";

    public SessionRecyclerViewAdapter(Context context, RealmResults<Session> realmResults, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public SessionRecyclerViewAdapter.ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.fragment_session_item, viewGroup, false);
        return new ViewHolder((LinearLayout) view);
    }

    @Override
    public void onBindRealmViewHolder(SessionRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        final Session session = realmResults.get(i);
        String name = "";
        for (Friend friend : session.getFriends()) {
            name += friend.getUser_id();
        }
        viewHolder.itemView.setOnClickListener(new OnItemClickListener(i));
        viewHolder.name.setText(name);
        viewHolder.avatar.setImageResource(R.drawable.ic_textsms_24dp);
        viewHolder.message.setText(session.getUpdate_time().toString());
    }

    public class ViewHolder extends RealmViewHolder {

        ImageView avatar;
        TextView name;
        TextView message;

        public ViewHolder(LinearLayout container) {
            super(container);
            this.avatar = (ImageView) container.findViewById(R.id.session_avatar);
            this.name = (TextView) container.findViewById(R.id.session_name);
            this.message = (TextView) container.findViewById(R.id.session_message);
        }
    }

    private class OnItemClickListener implements View.OnClickListener {

        private String session_uuid;

        OnItemClickListener(int position) {
            session_uuid = realmResults.get(position).getUuid();
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MessageActivity.class);
            intent.putExtra(SESSION_UUID,session_uuid);
            view.getContext().startActivity(intent);
        }
    }
}
