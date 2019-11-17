package iron2014.bansachonline.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import iron2014.bansachonline.R;


public class EditGioHangActivity extends AppCompatActivity {

    EditText edtmasp, edtsanpham, edtgia, edtsoluong, edttongtien;
    String masp, sanpham , gia, soluong, tongtien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gio_hang);

        edtmasp = findViewById(R.id.edtmaSanPhamKH);
        edtsanpham = findViewById(R.id.edtSanPhamKH);
        edtgia = findViewById(R.id.edtGiasp);
        edtsoluong = findViewById(R.id.txtSL);
        edttongtien = findViewById(R.id.edtTiensp);

        Intent intent = getIntent();
        masp = intent.getStringExtra("masp");
        sanpham = intent.getStringExtra("sanpham");
        gia = intent.getStringExtra("gia");
        soluong = intent.getStringExtra("soluong");
        tongtien = intent.getStringExtra("tongtien");

        edtmasp.setText(masp);
        edtsanpham.setText(sanpham);
        edtgia.setText(gia);
        edttongtien.setText(tongtien);
        edtsoluong.setText(soluong);
    }
}
