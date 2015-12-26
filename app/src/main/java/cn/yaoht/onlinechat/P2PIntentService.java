package cn.yaoht.onlinechat;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import cn.yaoht.onlinechat.midware.JsonSerializer;
import cn.yaoht.onlinechat.model.Message;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class P2PIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_LISTEN = "cn.yaoht.onlinechat.action.LISTEN";

    public P2PIntentService() {
        super("P2PIntentService");
    }

    public static void startActionListen(Context context) {
        Intent intent = new Intent(context, P2PIntentService.class);
        intent.setAction(ACTION_LISTEN);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LISTEN.equals(action)) {
                handleActionListen();
            }
        }
    }

    private void handleActionListen() {
        new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket server = new ServerSocket(53000);
                    //noinspection InfiniteLoopStatement
                    while (true) {
                        Socket socket = server.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String str = in.readLine();
                        Log.v("Service", str);
                        JsonSerializer.JsontoMessage(str);
                        in.close();
                        socket.close();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }.run();
    }
}
