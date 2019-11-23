package iron2014.bansachonline.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import static android.content.ContentValues.TAG;
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    SessionManager sessionManager;
    public static final int ID_BIG_NOTIFICATION = 1;
    public static final int ID_SMALL_NOTIFICATION = 2;
    @Override
    public void onCreate() {
        super.onCreate();
        sessionManager = new SessionManager(getApplicationContext());
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData() != null) {
            enviarNotificacion(remoteMessage);
        }
        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "Body notification: "+remoteMessage.getNotification().getBody());
            enviarNotificacion(remoteMessage);
        }
    }

    private void enviarNotificacion(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "xcheko51x";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Solo para android Oreo o superior
            @SuppressLint("WrongConstant")
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Mi notificacion",
                    NotificationManager.IMPORTANCE_MAX
            );

            // Configuracion del canal de notificacion
            channel.setDescription("xcheko51x channel para app");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);

            manager.createNotificationChannel(channel);

        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        String check = "4";
        intent.putExtra("check", check);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                ID_BIG_NOTIFICATION,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(body);
        bigPictureStyle.bigPicture(getBitmapFromURL("http://thuvienkontum.vn/uploads/news/2016_07/sach_1.jpg"));
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        Notification notification;
        Uri sound = Uri.parse("android.resource://"
                + getApplicationContext().getPackageName() + "/" + R.raw.drum);
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_launcher))
                .setContentText(body)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//
//        builder.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.khuyenmai_x32)
//                .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.drawable.sach3))
//                .setTicker("Hearty365")
//                .setContentTitle(title)
//                .setContentText(body)
//                .setVibrate(new long[]{0, 1000, 500, 1000})
//                .setContentIntent(pendingIntent)
//                .setContentInfo("info");
//
//        manager.notify(1, builder.build());
    }

    @Override
    public void onNewToken(String token) {
        Log.d("Token", "Refreshed Token"+token);
        FirebaseMessaging.getInstance().subscribeToTopic("dispositivos");
        sessionManager.createToken(token);
    }

    //The method will return Bitmap from an image URL
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
