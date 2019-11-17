package iron2014.bansachonline.Activity.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import iron2014.bansachonline.R;


public class HoadonActivity extends AppCompatActivity {
    Button btn_themhd,btn_huy;
    EditText edmasp,edtensach,edgiasach,edsoluong,edngayxuat,edtongtien;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadon);
        Intent intent = getIntent();
        String masach = intent.getStringExtra("masach");
        String tensach = intent.getStringExtra("tensach");
        String giaban = intent.getStringExtra("gia");
        Anhxa();
////        setTitle("Hóa đơn");
////        final Intent intent = getIntent();
////        final String masach = intent.getStringExtra("masach");
////        final String tensach = intent.getStringExtra("tensach");
////        final String giaban=intent.getStringExtra("giaban");
        Toast.makeText(this, "có:"+tensach, Toast.LENGTH_SHORT).show();
////
        edmasp.setText(masach);
        edtensach.setText(tensach );
        edgiasach.setText(giaban);


        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
     private void Anhxa(){
        edmasp=findViewById(R.id.edmasp);
        edtensach=findViewById(R.id.edtensach);
        edgiasach=findViewById(R.id.edgiaban);
        edsoluong=findViewById(R.id.edsoluong);
        edngayxuat=findViewById(R.id.edngayxuat);
        edtongtien=findViewById(R.id.edtongtien);
        btn_themhd=findViewById(R.id.btn_themhoadon);
        btn_huy=findViewById(R.id.btn_huy);
     }

}
