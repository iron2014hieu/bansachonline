package iron2014.bansachonline.Activity.hoadon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import iron2014.bansachonline.fragmentVanChuyen.Activity.ShipperActivity;
import iron2014.bansachonline.LoginRegister.ProfileActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.fragmentVanChuyen.Activity.ChitietGiaoHangActivity;
import iron2014.bansachonline.fragmentVanChuyen.Activity.ChitietVanChuyenActivity;


public class UpdateProfileActivity extends AppCompatActivity {

    EditText edtNameUser,edtEmailUser,edtSdtUser,edtNgaySinh,edtGioiTinh, edtAddress, edtid;
    Button btnUpdateUser;
    String URL_UDATE = "https://bansachonline.xyz/bansach/user/update.php";
    String id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        Anhxa();

        Intent intent = getIntent();

        String email = intent.getStringExtra("email");
        final String name = intent.getStringExtra("name");
        final String address = intent.getStringExtra("address");
        final String phone = intent.getStringExtra("phone");
        final String ngaysinh = intent.getStringExtra("ngaysinh");
        final String sex = intent.getStringExtra("sex");

        id = intent.getStringExtra("id");

        edtid.setText(id);
        edtNameUser.setText(name);
        edtEmailUser.setText(email);
        edtSdtUser.setText(phone);
        edtNgaySinh.setText(ngaysinh);
        edtGioiTinh.setText(sex);
        edtAddress.setText(address);

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name1 = edtNameUser.getText().toString();
                final String address1 = edtAddress.getText().toString();
                final String sex1 = edtGioiTinh.getText().toString();
                final String ngaysinh1 = edtNgaySinh.getText().toString();
                final String phone1 = edtSdtUser.getText().toString();
                saveDetail(id, name1, address1, sex1, ngaysinh1, phone1);
            }
    });
    }

    private void saveDetail(final String strid, final String strname, final String strdiachi, final String strsex, final String strngaysinh, final String strphone){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_UDATE+"?id="+strid+"&name="
                +strname+"&address="+strdiachi+"&sex="+strsex+"&ngaysinh="+strngaysinh+"&phone="+strphone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.equals("success")){
                            Toast.makeText(UpdateProfileActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("Error: ", error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void Anhxa() {
        edtid = findViewById(R.id.edtid);
        edtNameUser = findViewById(R.id.edtNameUser);
        edtEmailUser = findViewById(R.id.edtEmailUser);
        edtSdtUser = findViewById(R.id.edtSdtUser);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtGioiTinh = findViewById(R.id.edtGioiTinh);
        edtAddress = findViewById(R.id.edtAddress);
        btnUpdateUser = findViewById(R.id.btnUpdateUser);
    }
//    private void UpdateTinhtrang(final String name , final String address , final String sex, final String ngaysinh , final String phone ,  String url){
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(UpdateProfileActivity.this, ""+response, Toast.LENGTH_SHORT).show();
//                        if (response.equals("success")){
//                            Toast.makeText(UpdateProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplication(), ProfileActivity.class);
//                            startActivity(intent);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("update tt er ", error.toString());
//            }
//        })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put("id", id);
//                params.put("name", name);
//                params.put("address", address);
//                params.put("sex", sex);
//                params.put("ngaysinh", ngaysinh);
//                params.put("phone", phone);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(request);
//    }
}
