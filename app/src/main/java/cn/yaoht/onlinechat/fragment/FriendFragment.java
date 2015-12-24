package cn.yaoht.onlinechat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        realmRecyclerView = (RealmRecyclerView) view.findViewById(R.id.friend_realm_recycler_view);
        realm = Realm.getInstance(getContext());
        RealmResults<Friend> friends = realm.where(Friend.class).findAll();
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(getContext(), friends, true, true);
        return view;
    }

}
