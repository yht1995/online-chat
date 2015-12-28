package cn.yaoht.onlinechat.midware;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

import cn.yaoht.onlinechat.Utility;
import cn.yaoht.onlinechat.model.Friend;

/**
 * Created by yaoht on 2015/12/10.
 * Project: OnlineChat
 */
public class ServerMidware {

    private static final String LOGIN_SUCCESSFUL = "lol";
    private static final String LOGOUT_SUCCESSFUL = "loo";

    private static ServerMidware instance;
    private String ip_address;
    private int port;
    private String username;
    private String password;
    private Boolean is_online;
    private OnlineStateChangedListener onlineStateChangedListener;

    public ServerMidware(String ip_address, int port) {
        this.ip_address = ip_address;
        this.port = port;
        this.is_online = false;
        instance = this;
    }

    public static ServerMidware getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void Login() throws Exception {
        Login(username, password);
    }

    public void Login(String username, String password) throws Exception {
        if (!Utility.CheckUserID(username)) {
            throw new Exception("UserId Illegal");
        }
        this.username = username;
        this.password = password;
        new LoginAsync().execute(username + "_" + password);
    }

    public void Logout() {
        Logout(this.username);
    }


    public void Logout(String username) {
        new LoginAsync().execute("logout" + username);
    }

    public String QueryOnlineState(Friend friend) {
        String ip = ServerSocket("q" + friend.getUser_id());
        if (Utility.CheckIPAddress(ip)) {
            return ip;
        } else {
            return "n";
        }
    }

    public void setOnlineStateChangedListener(OnlineStateChangedListener onlineStateChangedListener) {
        this.onlineStateChangedListener = onlineStateChangedListener;
    }

    public Boolean getIs_online() {
        return is_online;
    }

    private String ServerSocket(String cmd) {
        try {
            Socket socket = new Socket(ip_address, port);
            PrintWriter out = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream())), true);
            out.print(cmd);
            out.flush();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            char[] back_msg = new char[30];
            int length = in.read(back_msg);
            socket.close();
            return String.valueOf(back_msg, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public interface OnlineStateChangedListener {
        void ChangeOnlineState(boolean is_online);
    }

    private class LoginAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return ServerSocket(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (Objects.equals(s, LOGIN_SUCCESSFUL)) {
                is_online = true;
                onlineStateChangedListener.ChangeOnlineState(true);
            } else if (Objects.equals(s, LOGOUT_SUCCESSFUL)) {
                is_online = false;
                onlineStateChangedListener.ChangeOnlineState(false);
            }
        }
    }

}
