package cn.yaoht.onlinechat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.yaoht.onlinechat.midware.ServerMidware;

public class LoginActivity extends AppCompatActivity {

    private ServerMidware serverMidware;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button_login = (Button) findViewById(R.id.activity_login_button_login);
        final EditText edittext_userid = (EditText) findViewById(R.id.activity_login_edittext_user_id);
        final EditText edittext_password = (EditText) findViewById(R.id.activity_login_edittext_password);

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
                serverMidware.Login(edittext_userid.getText().toString(), edittext_password.getText().toString());
            }
        });
    }
}
