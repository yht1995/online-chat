package cn.yaoht.onlinechat.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.model.Friend;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by yaoht on 2015/12/24.
 * Project: OnlineChat
 */
public class FriendRecyclerViewAdapter extends RealmBasedRecyclerViewAdapter<Friend,
        FriendRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Boolean> checked_list;

    public FriendRecyclerViewAdapter(Context context, RealmResults<Friend> realmResults, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
        checked_list = new ArrayList<>();
    }

    public ArrayList<Boolean> getChecked_list() {
        return checked_list;
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.fragment_friend_item, viewGroup, false);
        return new ViewHolder((LinearLayout) view);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int pos) {
        final Friend friend = realmResults.get(pos);

        viewHolder.name.setText(friend.getUser_id());
        checked_list.add(pos, viewHolder.checkBox.isChecked());
        viewHolder.checkBox.setOnClickListener(new OnItemClickListener(pos));

        if (friend.getOn_line()) {
            viewHolder.description.setText(friend.getIp_address());
            viewHolder.avatar.setImageResource(R.drawable.ic_person_48dp);
        } else {
            viewHolder.description.setText(R.string.outline);
            viewHolder.avatar.setImageResource(R.drawable.ic_person_outline_48dp);
        }

    }


    public class ViewHolder extends RealmViewHolder {

        ImageView avatar;
        TextView name;
        TextView description;
        CheckBox checkBox;

        public ViewHolder(LinearLayout container) {
            super(container);
            this.avatar = (ImageView) container.findViewById(R.id.friend_avatar);
            this.name = (TextView) container.findViewById(R.id.friend_name);
            this.description = (TextView) container.findViewById(R.id.friend_description);
            this.checkBox = (CheckBox) container.findViewById(R.id.friend_checkbox);
        }
    }

    private class OnItemClickListener implements CheckBox.OnClickListener {
        private int pos;

        OnItemClickListener(int position) {
            this.pos = position;
        }

        @Override
        public void onClick(View v) {
            checked_list.set(pos, ((CheckBox) v).isChecked());
        }
    }
}
