package iron2014.bansachonline.LoginRegister;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;

import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;


public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edtNameUser, edtEmailUser, edtSdtUser, edtNgaySinh, edtGioiTinh, edtAddress, edtid;
    Button btnUpdateUser;
    ImageButton btnDatePicker;
    String URL_UDATE = "https://bansachonline.xyz/bansach/user/update.php";
    String id;

    RadioGroup radioGroup;
    SessionManager sessionManager;

    private int mYear, mMonth, mDay;
    String name,address,phone,ngaysinh,sex,email, quyen;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        Anhxa();

        Intent intent = getIntent();
        sessionManager = new SessionManager(getApplicationContext());
         name= intent.getStringExtra("name");
         address = intent.getStringExtra("address");
         phone = intent.getStringExtra("phone");
         ngaysinh = intent.getStringExtra("ngaysinh");
         sex = intent.getStringExtra("sex");

        id = intent.getStringExtra("id");

        HashMap<String, String> user = sessionManager.getUserDetail();
        quyen = user.get(sessionManager.QUYEN);
        email = user.get(sessionManager.EMAIL);

        edtid.setText(id);
        edtNameUser.setText(name);
        edtEmailUser.setText(email);
        edtSdtUser.setText(phone);
        edtNgaySinh.setText(ngaysinh);
        edtGioiTinh.setText(sex);
        edtAddress.setText(address);
        btnDatePicker = findViewById(R.id.btn_date);
        btnDatePicker.setOnClickListener(this);
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

        radioGroup = findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_nam:
                        edtGioiTinh.setText(getString(R.string.nam));
                        break;
                    case R.id.rb_nu:
                        edtGioiTinh.setText(getString(R.string.nu));
                        break;
                }
            }
        });
    }
    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            edtNgaySinh.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }
    private void saveDetail(final String strid, final String strname, final String strdiachi, final String strsex, final String strngaysinh, final String strphone) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_UDATE + "?id=" + strid + "&name="
                + strname + "&address=" + strdiachi + "&sex=" + strsex + "&ngaysinh=" + strngaysinh + "&phone=" + strphone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            sessionManager.createSession(id, email,address,phone, name, quyen);
                            startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
                            Toast.makeText(UpdateProfileActivity.this, getString(R.string.suatc), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
