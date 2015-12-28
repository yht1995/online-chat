package cn.yaoht.onlinechat.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.midware.ServerMidware;
import cn.yaoht.onlinechat.model.Friend;
import cn.yaoht.onlinechat.model.Message;
import cn.yaoht.onlinechat.model.Session;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity implements ClearDBWarringDialog.NoticeDialogListener {

    static final String PREFS_NAME = "SETTING";
    private ServerMidware serverMidware;
    private SharedPreferences settings;
    private String userid;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button_login = (Button) findViewById(R.id.activity_login_button_login);
        final EditText edittext_userid = (EditText) findViewById(R.id.activity_login_edittext_user_id);
        final EditText edittext_password = (EditText) findViewById(R.id.activity_login_edittext_password);

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        edittext_userid.setText(settings.getString("userid", ""));
        edittext_password.setText(settings.getString("password", ""));


        serverMidware = ServerMidware.getInstance();
        serverMidware.setOnlineStateChangedListener(new ServerMidware.OnlineStateChangedListener() {
            @Override
            public void ChangeOnlineState(boolean is_online) {
                if (is_online) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                userid = edittext_userid.getText().toString();
                password = edittext_password.getText().toString();

                if (!Objects.equals(userid, settings.getString("userid", "")) && settings.contains("userid")) {
                    FragmentManager fm = getSupportFragmentManager();
                    ClearDBWarringDialog warringDialog = new ClearDBWarringDialog();
                    warringDialog.show(fm, "clear warring");
                } else {
                    if (!settings.contains("userid")) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("userid", userid);
                        editor.putString("password", password);
                        editor.apply();
                    }
                    try {
                        serverMidware.Login(userid, password);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.clear(Friend.class);
                bgRealm.clear(Session.class);
                bgRealm.clear(Message.class);
            }
        }, null);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userid", userid);
        editor.putString("password", password);
        editor.apply();
        try {
            serverMidware.Login(userid, password);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        setResult(RESULT_CANCELED);
        finish();
    }


}

