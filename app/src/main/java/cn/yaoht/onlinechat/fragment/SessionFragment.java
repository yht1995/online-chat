package cn.yaoht.onlinechat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.model.Session;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by yaoht on 2015/12/27.
 * Project: OnlineChat
 */

/**
 * @author yaoht
 *         A simple {@link Fragment} subclass.
 *         会话列表
 */
public class SessionFragment extends Fragment {

    private RealmRecyclerView realmRecyclerView;
    private SessionRecyclerViewAdapter sessionRecyclerViewAdapter;
    private Realm realm;
    private RealmResults<Session> sessions;

    public SessionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_session, container, false);
        realmRecyclerView = (RealmRecyclerView) view.findViewById(R.id.fragment_session_realm_recycler_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        sessions = realm.where(Session.class).findAll();
        sessions.sort("update_time", Sort.DESCENDING);
        sessionRecyclerViewAdapter = new SessionRecyclerViewAdapter(getContext(),sessions,true,true);
        realmRecyclerView.setAdapter(sessionRecyclerViewAdapter);
    }

}
