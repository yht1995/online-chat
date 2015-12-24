package cn.yaoht.onlinechat;

/**
 * Created by yaoht on 2015/12/4.
 * Project: OnlineChat
 */

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class OnlineChat extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Configure Realm for the application
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default
        Realm realm = Realm.getDefaultInstance();
    }
}
