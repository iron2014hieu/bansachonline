package iron2014.bansachonline.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MaGiamGia extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String ma = intent.getStringExtra("MA");
        if(ma.length()>3 && ma.length() <=9){
            String km = ma.substring(0,3);
            if (ma.length()==9) {
                if (km.equalsIgnoreCase("MEM")) {
                    if (Integer.parseInt(ma.substring(3, 9)) == 537128) {
                        Toast.makeText(context, "Giảm giá 10%", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(ma.substring(3, 9)) == 537129) {
                        Toast.makeText(context, "Giảm giá 20%", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Mã nhập dung !", Toast.LENGTH_SHORT).show();
                    }
                } else if (km.equalsIgnoreCase("VIP")) {
                    if (Integer.parseInt(ma.substring(3, 9)) == 537128) {
                        Toast.makeText(context, "Giảm giá 30%", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(km.substring(3, 9)) == 537129) {
                        Toast.makeText(context, "Giảm giá 50%", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Mã nhập dung", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Mã giảm giá bắt đầu bằng MEN hoặc VIP", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(context, "Mã phải đủ 9 kí tư", Toast.LENGTH_SHORT).show();
        }
    }
}
