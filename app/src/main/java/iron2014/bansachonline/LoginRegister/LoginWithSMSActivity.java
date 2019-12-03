package iron2014.bansachonline.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import iron2014.bansachonline.URL.EndPoints;
import iron2014.bansachonline.fragmentVanChuyen.Activity.ShipperActivity;
import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.URL.UrlSql;

public class LoginWithSMSActivity extends AppCompatActivity {
    private String verificationId,phonenumber,sodienthoai;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar,progess_success_login;
    private EditText editText;
    private TextView textView;
    SessionManager sessionManager;
    String token;
    Button buttonSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_sms);
        sessionManager = new SessionManager(this);
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        progess_success_login= findViewById(R.id.progess_success_login);
        editText = findViewById(R.id.editTextCode);
        textView=findViewById(R.id.textView);
        buttonSignIn= findViewById(R.id.buttonSignIn);

        phonenumber = getIntent().getStringExtra("phonenumber");
        sodienthoai = getIntent().getStringExtra("sodienthoai");
        sendVerificationCode(phonenumber);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){
                            token = task.getResult().getToken();
                        }
                    }
                });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError(getString(R.string.nhapma));
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            textView.setVisibility(View.GONE);
                            buttonSignIn.setVisibility(View.GONE);
                            editText.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progess_success_login.setVisibility(View.VISIBLE);
                            LoginWithphone(sodienthoai);
                        } else {
                            String massage = task.getException().getMessage();
                            //Toast.makeText(LoginWithSMSActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("massage",massage);
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginWithSMSActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    private void LoginWithphone(final String phone){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlSql.URL_LOGIN_PHONE+phone,
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
                                    String address = object.getString("address");
                                    String phone = object.getString("phone").trim();
                                    String id = object.getString("id").trim();
                                    String quyen = object.getString("quyen").trim();
                                    sessionManager.createSession(id, email,address,phone, name, quyen);
                                    updateDevicesToken("1", id);
                                    if(quyen.equals("shipper")){
                                        startActivity(new Intent(LoginWithSMSActivity.this, ShipperActivity.class));
                                    }else {
                                        startActivity(new Intent(LoginWithSMSActivity.this, MainActivity.class));
                                        Toast.makeText(LoginWithSMSActivity.this, getString(R.string.dangnhaptc), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else {
                                Toast.makeText(LoginWithSMSActivity.this, getString(R.string.banchuadksdtnay), Toast.LENGTH_SHORT).show();
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
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void updateDevicesToken(final String islogin,final String mauser){

        if (token == null) {
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_UPDATE_DEVICES_ISLOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("msg", obj.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mauser", mauser);
                params.put("token", token);
                params.put("islogin", islogin);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
