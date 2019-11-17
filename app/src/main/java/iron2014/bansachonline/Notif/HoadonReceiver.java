package iron2014.bansachonline.Notif;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class HoadonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        String message = intent.getStringExtra("toastMessage");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        // nhớ đăng ký bên maintifest nữa nha kk
        if (remoteInput != null) {

//            CartDetailActivity.sendChannel1Notification(context);
        }
    }
}
