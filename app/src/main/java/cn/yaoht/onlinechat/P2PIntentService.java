package cn.yaoht.onlinechat;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import cn.yaoht.onlinechat.midware.MessageMidware;
import cn.yaoht.onlinechat.midware.RawMessage;
import cn.yaoht.onlinechat.midware.Serializer;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class P2PIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final int P2P_PORT = 53000;
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
                    ServerSocket server = new ServerSocket(P2P_PORT);
                    //noinspection InfiniteLoopStatement
                    while (true) {
                        Socket socket = server.accept();
                        new ListenAsync().execute(socket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.run();
    }

    private void OnNewMessageArrived(RawMessage rawMessage) {
        MessageMidware messageMidware = new MessageMidware();
        messageMidware.ReceiveMessage(rawMessage);
    }

    private class ListenAsync extends AsyncTask<Socket, Void, RawMessage> {

        @Override
        protected RawMessage doInBackground(Socket... params) {
            try {
                Socket socket = params[0];
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String str = in.readLine();
                Log.i("receive", str);
                in.close();
                socket.close();
                return Serializer.JsontoMessage(str);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(RawMessage rawMessage) {
            OnNewMessageArrived(rawMessage);
        }
    }
}
