package cn.yaoht.onlinechat.backend;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.activity.MessageActivity;
import cn.yaoht.onlinechat.midware.MessageMidware;
import cn.yaoht.onlinechat.midware.RawMessage;
import cn.yaoht.onlinechat.midware.Serializer;


/**
 * @author yaoht
 * @see android.app.IntentService
 *
 * 服务：核心服务
 * TcpServer 接收消息
 */
public class CoreService extends IntentService {

    /**
     * P2P端口
     */
    public static final int P2P_PORT = 53000;
    private static final String ACTION_LISTEN = "cn.yaoht.onlinechat.action.LISTEN";
    private static final int NOTICE_NEW_MESSAGE_NUM = 1;

    public CoreService() {
        super("CoreService");
    }

    public static void startActionListen(Context context) {
        Intent intent = new Intent(context, CoreService.class);
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

    /**
     * 接收消息
     * @param rawMessage 原始消息
     */
    private void OnNewMessageArrived(RawMessage rawMessage) {
        MessageMidware messageMidware = new MessageMidware();
        messageMidware.ReceiveMessage(rawMessage);

        BuildNotification(rawMessage);
    }

    /**
     * 显示系统通知
     * @param rawMessage 消息
     */
    private void BuildNotification(RawMessage rawMessage) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_textsms_24dp)
                        .setAutoCancel(true)
                        .setContentTitle(rawMessage.from_friend)
                        .setContentText(rawMessage.getContentDescription());

        Intent resultIntent = new Intent(this, MessageActivity.class);
        resultIntent.putExtra(MessageActivity.SESSION_UUID, rawMessage.session_uuid);
        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTICE_NEW_MESSAGE_NUM, mBuilder.build());
    }


    /**
     * @author yaoht
     * 异步任务
     * 接收消息
     */
    private class ListenAsync extends AsyncTask<Socket, Void, RawMessage> {

        @Override
        protected RawMessage doInBackground(Socket... params) {
            try {
                Socket socket = params[0];
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String str = in.readLine();
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
