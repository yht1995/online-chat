package cn.yaoht.onlinechat.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yaoht.onlinechat.R;
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


    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        realmRecyclerView = (RealmRecyclerView) view.findViewById(R.id.fragment_friend_realm_recycler_view);
        realm = Realm.getInstance(getContext());
        RealmResults<Friend> friends = realm.where(Friend.class).findAll();
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(getContext(), friends, true, true);
        realmRecyclerView.setAdapter(friendRecyclerViewAdapter);

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


}
