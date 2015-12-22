package cn.yaoht.onlinechat;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import cn.yaoht.onlinechat.model.Friend;
import io.realm.RealmResults;

/**
 * Created by yaoht on 2015/12/10.
 * Project: OnlineChat
 */
public class ServerBackend {
    private String ip_address;
    private int port;
    private Boolean is_online;

    public ServerBackend(String ip_address, int port) {
        this.ip_address = ip_address;
        this.port = port;
    }

    public void Login(String username, String password) {
        new LoginAsync().execute(username + "_" + password);
    }

    public void Logout(String username) {
        new LoginAsync().execute("logout" + username);
    }

    public void QueryOnlineState(RealmResults<Friend> friends) {

    }

    protected void QueryOnlineStateCallback(String username, String ip_address) {

    }

    protected void OnOnlineStateChangedCallback(Boolean is_online) {
        this.is_online = is_online;
    }

    public Boolean getIs_online() {
        return is_online;
    }


    private class QueryOnlineStateAsync extends AsyncTask<Friend, Void, String> {

        @Override
        protected String doInBackground(Friend... params) {
            try {
                Socket socket = new Socket(ip_address, port);
                PrintWriter out = new PrintWriter(
                        new BufferedWriter(new OutputStreamWriter(
                                socket.getOutputStream())), true);
                out.print("q" + params[0].getUser_id());
                out.flush();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                char[] back_msg = new char[20];
                int length = in.read(back_msg);
                socket.close();
                return String.valueOf(back_msg, 0, length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "n";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }


    private class LoginAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Socket socket = new Socket(ip_address, port);
                PrintWriter out = new PrintWriter(
                        new BufferedWriter(new OutputStreamWriter(
                                socket.getOutputStream())), true);
                out.print(params[0]);
                out.flush();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                char[] back_msg = new char[20];
                int length = in.read(back_msg);
                socket.close();
                return String.valueOf(back_msg, 0, length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s) {
                case "lol":
                    OnOnlineStateChangedCallback(true);
                    break;
                case "loo":
                    OnOnlineStateChangedCallback(false);
                    break;
            }
        }
    }
}
