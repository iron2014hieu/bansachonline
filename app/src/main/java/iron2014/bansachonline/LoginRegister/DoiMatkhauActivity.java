package iron2014.bansachonline.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.URL.UrlSql;
import iron2014.bansachonline.fragmentVanChuyen.Activity.ShipperActivity;

public class DoiMatkhauActivity extends AppCompatActivity {
    TextInputEditText edtPassRE,edtEmailRE;
    Button btnTieptuc;
    SessionManager sessionManager;
    String email ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_matkhau);
        edtEmailRE=findViewById(R.id.edtEmailRE);
        edtPassRE=findViewById(R.id.edtPasswordRE);
        btnTieptuc = findViewById(R.id.buttonTieptuc);
        Toolbar toolbar = findViewById(R.id.toolbar_doimk);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetail();
        email = user.get(sessionManager.EMAIL);
        edtEmailRE.setText(email);
        btnTieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = edtEmailRE.getText().toString().trim();
                String mPassword = edtPassRE.getText().toString().trim();
                if (mEmail.isEmpty() || mPassword.isEmpty()){
                    edtEmailRE.setError(getString(R.string.vuilongnhapemail));
                    edtPassRE.setError(getString(R.string.vuilongnhapmatkhau));
                }else {
                    Login();
                }
            }
        });
    }
    public void Back(View view) {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return super.onSupportNavigateUp();
    }

    private void Login(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                            if (success.equals("1")){
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String address = object.getString("address").trim();
                                    String phone = object.getString("phone").trim();
                                    String id = object.getString("id").trim();
                                    String quyen = object.getString("quyen").trim();

                                Intent intent = new Intent(getApplicationContext(),NewPasswordActivity.class);
                                intent.putExtra("mauser", id);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(), getString(R.string.saitk_ormk), Toast.LENGTH_SHORT).show();
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
                params.put("email", edtEmailRE.getText().toString().trim());
                params.put("password", edtPassRE.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
