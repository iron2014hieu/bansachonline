package iron2014.bansachonline.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import iron2014.bansachonline.Main2Activity;
import iron2014.bansachonline.R;

public class DatmuaActivity extends AppCompatActivity {

    EditText edtmaSp, edtTensp, edtGiasp, edtSL, edtTongtien;
    Button btnTangsl, btnGiamsp, btnDatmua;

    int giaBan = 1;
    private  int giaTri = 1;
    String URL_INSERT ="https://bansachonline.xyz/bansach/giohang/create_carts.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datmua);
        Toolbar toolbar = findViewById(R.id.toolbardm);
        ActionBar actionBar = getSupportActionBar();
        toolbar.setTitle("Đặt mua sản phẩm");

        Intent intent = getIntent();
        final String masach = intent.getStringExtra("masach");
        final String tensach = intent.getStringExtra("tensach");
        final String linkAnh = intent.getStringExtra("hinhanhsach");
        String giaban = intent.getStringExtra("gia");
        final String iduser = intent.getStringExtra("mauser");

        edtmaSp = findViewById(R.id.edtmaSanPhamKH);
        edtTensp = findViewById(R.id.edtSanPhamKH);
        edtGiasp = findViewById(R.id.edtGiasp);
        edtSL = findViewById(R.id.txtSL);
        edtTongtien = findViewById(R.id.edtTiensp);

        btnGiamsp = findViewById(R.id.btnGiamSL);
        btnTangsl = findViewById(R.id.btnThemSL);
        btnDatmua = findViewById(R.id.btnDatmuaSp);

        giaBan = Integer.valueOf(giaban);
        edtmaSp.setText(masach);
        edtTensp.setText(tensach );
        edtGiasp.setText(giaban);
        edtTongtien.setText(giaban);



        btnTangsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TangSL();
            }
        });
        btnGiamsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiamSL();
            }
        });


        btnDatmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemDatmua(masach, tensach, linkAnh,iduser);
            }
        });
    }
    private void TangSL(){
        giaTri++;
        edtSL.setText(String.valueOf(giaTri));
        edtTongtien.setText(String.valueOf(giaBan*giaTri));
    }
    private void GiamSL(){
        if(giaTri>1){
            giaTri--;
            edtSL.setText(String.valueOf(giaTri));
            edtTongtien.setText(String.valueOf(giaBan*giaTri));
        }
    }
    private void ThemDatmua(final String masach, final String sp, final String hinhanhsach, final String mauser){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String check = jsonObject.getString("check");

                            if(check.equals("chuatontai")){

                                if (success.equals("1")){
//                                    Toast.makeText(Main2Activity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                                }else {
//                                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(DatmuaActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("printStackTrace", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DatmuaActivity.this, "Loi roi nhe", Toast.LENGTH_SHORT).show();
                Log.d("MYSQL", "Lỗi! \n" +error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("masach", masach);
                params.put("sanpham", sp);
                params.put("hinhanh", hinhanhsach);
                params.put("gia", edtGiasp.getText().toString().trim());
                params.put("soluong", edtSL.getText().toString().trim());
                params.put("tongtien", edtTongtien.getText().toString().trim());
                params.put("mauser", mauser);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
