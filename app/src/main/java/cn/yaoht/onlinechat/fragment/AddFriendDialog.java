package cn.yaoht.onlinechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.model.Friend;
import io.realm.Realm;

public class AddFriendDialog extends DialogFragment {

    private Realm realm;
    private EditText edit_friend_id;

    public AddFriendDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddFriendDialog newInstance() {
        return new AddFriendDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_friend_add, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        realm = Realm.getInstance(getContext());

        // Get field from view
        edit_friend_id = (EditText) view.findViewById(R.id.dialog_add_friend_edittext_user_id);
        // Show soft keyboard automatically and request focus to field
        edit_friend_id.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Button button_add_friend = (Button) view.findViewById(R.id.dialog_add_friend_button_ok);
        button_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                Friend friend = realm.createObject(Friend.class);
                friend.setUser_id(edit_friend_id.getText().toString());
                friend.setIp_address("");
                friend.setOn_line(false);
                friend.setNick_name("");
                realm.commitTransaction();
                dismiss();
            }
        });
    }
}