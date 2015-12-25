package cn.yaoht.onlinechat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

import cn.yaoht.onlinechat.model.Friend;

/**
 * Created by yaoht on 2015/12/10.
 * Project: OnlineChat
 */
public class ServerBackend {

    private String ip_address;
    private int port;

    private static ServerBackend instance;

    public static ServerBackend getInstance() {
        return instance;
    }

    public ServerBackend(String ip_address, int port) {
        this.ip_address = ip_address;
        this.port = port;
        instance = this;
    }

    public boolean Login(String username, String password) {
        String result = ServerSocket(username + "_" + password);
        Log.i("server", result);
        return Objects.equals(result, "lol");
    }

    public boolean Logout(String username) {
        return Objects.equals(ServerSocket("logout" + username), "loo");
    }

    public String QueryOnlineState(Friend friend) {
        return ServerSocket("q" + friend.getUser_id());
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

}
