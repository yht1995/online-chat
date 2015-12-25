package cn.yaoht.onlinechat.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import cn.yaoht.onlinechat.R;
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


    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        realmRecyclerView = (RealmRecyclerView) view.findViewById(R.id.fragment_friend_realm_recycler_view);

        FloatingActionButton button_add_friend = (FloatingActionButton) view.findViewById(R.id.fragment_friend_button_friend_add);
        button_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                AddFriendDialog editNameDialog = AddFriendDialog.newInstance();
                editNameDialog.show(fm, "add friend");
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        friends = realm.where(Friend.class).findAll();
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(getContext(), friends, true, true);
        realmRecyclerView.setAdapter(friendRecyclerViewAdapter);
        realmRecyclerView.setOnRefreshListener(new OnFriendsFragmentRefreshListener());
    }

    @Override
    public void onResume() {
        super.onResume();
        RefreshFriendOnlineState();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
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
                friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(getContext(), friends, true, true);
                realmRecyclerView.setAdapter(friendRecyclerViewAdapter);
            }

            @Override
            public void onError(Exception e) {
                realmRecyclerView.setRefreshing(false);
            }
        });
    }

    private class OnFriendsFragmentRefreshListener implements RealmRecyclerView.OnRefreshListener {

        @Override
        public void onRefresh() {
            RefreshFriendOnlineState();
        }
    }
}
