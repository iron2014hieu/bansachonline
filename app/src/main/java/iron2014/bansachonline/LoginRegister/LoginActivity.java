package iron2014.bansachonline.LoginRegister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import iron2014.bansachonline.Service.FCM.SharedPrefManager;
import iron2014.bansachonline.URL.EndPoints;
import iron2014.bansachonline.fragmentVanChuyen.Activity.ShipperActivity;
import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.URL.UrlSql;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;

public class LoginActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String PASS = "passKey";
    public static final String REMEMBER = "remember";
    public SharedPreferences sharedpreferences;
    public CheckBox cbRemember;
    SharedPref sharedPref;
    UrlSql urlSql;
    private ProgressDialog progressDialog;
    EditText edtEmail, edtPassword;
    CircleImageView cicler_logo;
    Button btnLogin;
    ProgressBar progressBar;
    SessionManager sessionManager;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_login);
        addControls();
        sessionManager = new SessionManager(this);
        urlSql = new UrlSql();
        //khởi tạo shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loadData();//lấy dữ liệu đã lưu nếu có
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){
                            token = task.getResult().getToken();
                            edtPassword.setText(token);
                        }
                    }
                });
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mEmail = edtEmail.getText().toString().trim();
                    String mPassword = edtPassword.getText().toString().trim();
                    if (!mEmail.isEmpty() || !mPassword.isEmpty()){
                        if (cbRemember.isChecked()){
                            Login(mEmail,mPassword);
                            saveData(mEmail, mPassword);
                        }else {
                            Login(mEmail,mPassword);
                            clearData();
                        }
                    }else {
                        edtEmail.setError("Vui lòng nhập email");
                        edtPassword.setError("Vui lòng nhập password");
                    }
                }
            });
        }
        public void loginsms(View view){
        startActivity(new Intent(getBaseContext(), VerifyPhoneActivity.class));
        }
    private void Login(final String email, final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSql.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")){
                                for (int i =0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String address = object.getString("address").trim();
                                    String phone = object.getString("phone").trim();
                                    String id = object.getString("id").trim();
                                    String quyen = object.getString("quyen").trim();

                                    sessionManager.createSession(id, email,address,phone, name, quyen);
                                    //get token notif
                                    sendTokenToServer(id);

                                    if(quyen.equals("shipper")){
                                        startActivity(new Intent(LoginActivity.this, ShipperActivity.class));
                                    }else {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else {
                                Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Login error: ", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Login error: ", error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void regis(View view){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
    public void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }
    private void saveData(String username, String Pass) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASS, Pass);
        editor.putBoolean(REMEMBER,cbRemember.isChecked());
        editor.commit();
    }
    public void loadData() {
        if(sharedpreferences.getBoolean(REMEMBER,false)) {
            edtEmail.setText(sharedpreferences.getString(USERNAME, ""));
            edtPassword.setText(sharedpreferences.getString(PASS, ""));
            cbRemember.setChecked(true);
        }
        else
            cbRemember.setChecked(false);

    }
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
    private void addControls(){
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.loading_login);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        cicler_logo=(CircleImageView)findViewById(R.id.cicler_logo);
    }


    //storing token to mysql server
    private void sendTokenToServer(final String mauser) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        final String email = edtEmail.getText().toString();

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mauser", mauser);
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}



