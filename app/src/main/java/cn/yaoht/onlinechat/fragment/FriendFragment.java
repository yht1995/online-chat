package cn.yaoht.onlinechat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.activity.MessageActivity;
import cn.yaoht.onlinechat.midware.ServerMidware;
import cn.yaoht.onlinechat.model.Friend;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {

    private RealmRecyclerView realmRecyclerView;
    private FriendRecyclerViewAdapter friendRecyclerViewAdapter;
    private Realm realm;
    private RealmResults<Friend> friends;
    private Timer refresh_timer;


    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_friend, container, false);
        realmRecyclerView = (RealmRecyclerView) view.findViewById(R.id.fragment_friend_realm_recycler_view);

        final FloatingActionButton button_add_friend = (FloatingActionButton) view.findViewById(R.id.fragment_friend_button_friend_add);
        button_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                AddFriendDialog editNameDialog = AddFriendDialog.newInstance();
                editNameDialog.show(fm, "add friend");
            }
        });

        FloatingActionButton button_start_session = (FloatingActionButton) view.findViewById(R.id.fragment_friend_button_start_session);
        button_start_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Boolean> checked_list = friendRecyclerViewAdapter.getChecked_list();
                ArrayList<String> friend_list = new ArrayList<>();
                for (int i = 0; i < checked_list.size(); i++) {
                    if (checked_list.get(i)) {
                        friend_list.add(friends.get(i).getUser_id());
                    }
                }
                if (friend_list.size() == 0) {
                    Snackbar.make(button_add_friend, R.string.warring_at_least_one_friend, Snackbar.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), MessageActivity.class);
                    intent.putStringArrayListExtra(MessageActivity.SESSION_FRIEND, friend_list);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        friends = realm.where(Friend.class).findAll();
        friends.sort("user_id");
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(getContext(), friends, true, true);
        realmRecyclerView.setAdapter(friendRecyclerViewAdapter);
        realmRecyclerView.setOnRefreshListener(new RealmRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshFriendOnlineState();
            }
        });

        refresh_timer = new Timer();
        refresh_timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                RefreshFriendOnlineState();
            }
        }, 0, 60 * 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        refresh_timer.cancel();
    }

    private void RefreshFriendOnlineState() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                ServerMidware server = ServerMidware.getInstance();
                friends = bgRealm.where(Friend.class).findAll();
                for (int i = 0; i < friends.size(); i++) {
                    Friend friend = friends.get(i);
                    String ip = server.QueryOnlineState(friend);
                    if (Objects.equals(ip, "n")) {
                        friend.setOn_line(false);
                        friend.setIp_address("");
                    } else {
                        friend.setOn_line(true);
                        friend.setIp_address(ip);
                    }
                }
            }
        }, new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {
                realmRecyclerView.setRefreshing(false);
                friends = realm.where(Friend.class).findAll();
                friends.sort("user_id");
                friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(getContext(), friends, true, true);
                realmRecyclerView.setAdapter(friendRecyclerViewAdapter);
            }

            @Override
            public void onError(Exception e) {
                realmRecyclerView.setRefreshing(false);
            }
        });
    }
}
