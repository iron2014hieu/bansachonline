package iron2014.bansachonline.LoginRegister;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import iron2014.bansachonline.R;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtName, edtEmail, edtPassword, edtC_password;
    private Button btnRegis;
    private ProgressBar loading;
    private String URL_REGIS = "https://bansachonline.xyz/bansach/loginregister/register.php";

    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        theme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbarDangky);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        addControls();

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                btnRegis.setVisibility(View.GONE);
                final String name = edtName.getText().toString().trim();
                final String email = edtEmail.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btnRegis.setVisibility(View.VISIBLE);
                }else {
                    Regis();
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void Regis (){
        final String email = this.edtEmail.getText().toString().trim();
        final String password = this.edtPassword.getText().toString().trim();
        final String name = this.edtName.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String check = jsonObject.getString("check");

                            if(check.equals("chuatontai")){

                                if (success.equals("1")){
                                    loading.setVisibility(View.GONE);
                                    btnRegis.setVisibility(View.VISIBLE);
                                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("printStackTrace", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError regis ", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addControls(){
        loading = findViewById(R.id.loading);
        btnRegis=findViewById(R.id.btnRegister);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtC_password = findViewById(R.id.edtC_Password);
    }
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
}
