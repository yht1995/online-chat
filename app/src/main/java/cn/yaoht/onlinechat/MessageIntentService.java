package cn.yaoht.onlinechat;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class MessageIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_LISTEN = "cn.yaoht.onlinechat.action.LISTEN";
    private static final String ACTION_LOGIN = "cn.yaoht.onlinechat.action.LOGIN";
    private static final String ACTION_QUERY = "cn.yaoht.onlinechat.action.QUERY";

    private static final String EXTRA_PARAM_SERVER_IP = "cn.yaoht.onlinechat.extra.SERVER.IP";
    private static final String EXTRA_PARAM_SERVER_PORT = "cn.yaoht.onlinechat.extra.SERVER.PORT";
    private static final String EXTRA_PARAM_USERNAME = "cn.yaoht.onlinechat.extra.USERNAME";
    private static final String EXTRA_PARAM_PASSWORD = "cn.yaoht.onlinechat.extra.PASSWORD";

    public MessageIntentService() {
        super("MessageIntentService");
    }

    public static void startActionListen(Context context) {
        Intent intent = new Intent(context, MessageIntentService.class);
        intent.setAction(ACTION_LISTEN);
        context.startService(intent);
    }

    public static void startActionLogin(Context context, String server_ip, int server_port, String username, String password) {
        Intent intent = new Intent(context, MessageIntentService.class);
        intent.setAction(ACTION_LOGIN);
        intent.putExtra(EXTRA_PARAM_SERVER_IP, server_ip);
        intent.putExtra(EXTRA_PARAM_SERVER_PORT, server_port);
        intent.putExtra(EXTRA_PARAM_USERNAME, username);
        intent.putExtra(EXTRA_PARAM_PASSWORD, password);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LISTEN.equals(action)) {
                handleActionListen();
            } else if (ACTION_LOGIN.equals(action)) {
                final String server_ip = intent.getStringExtra(EXTRA_PARAM_SERVER_IP);
                final int server_port = intent.getIntExtra(EXTRA_PARAM_SERVER_PORT, 8000);
                final String username = intent.getStringExtra(EXTRA_PARAM_USERNAME);
                final String password = intent.getStringExtra(EXTRA_PARAM_PASSWORD);
                handleActionLogin(server_ip, server_port, username, password);
            }
        }
    }

    private void handleActionListen() {
        try {
            ServerSocket server = new ServerSocket(53000);
            byte[] buffer = new byte[1024];
            int bytes;
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String str = in.readLine();
                Log.v("Service",str);
                in.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleActionLogin(String server_ip, int server_port, String username, String password) {
        try {
            Socket socket = new Socket(server_ip,server_port);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
