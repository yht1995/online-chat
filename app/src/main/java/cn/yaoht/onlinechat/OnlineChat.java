package cn.yaoht.onlinechat;

/**
 * Created by yaoht on 2015/12/4.
 * Project: OnlineChat
 */

import android.app.Application;

import cn.yaoht.onlinechat.midware.ServerMidware;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class OnlineChat extends Application {

    public static final String SERVER_IP_ADDRESS = "166.111.140.14";
    public static final int SERVER_PORT = 8000;

    @Override
    public void onCreate() {
        super.onCreate();
        // Configure Realm for the application
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
//        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default

        new ServerMidware(SERVER_IP_ADDRESS, SERVER_PORT);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ServerMidware serverMidware = ServerMidware.getInstance();
        serverMidware.Logout();
    }
}

