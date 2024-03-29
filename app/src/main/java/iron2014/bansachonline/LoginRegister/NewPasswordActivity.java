package iron2014.bansachonline.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import java.util.regex.Pattern;

import iron2014.bansachonline.CustomToast;
import iron2014.bansachonline.R;
import iron2014.bansachonline.URL.UrlSql;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;

public class NewPasswordActivity extends AppCompatActivity {
    String id;
    Button buttonRepass;
    SharedPref sharedPref;
    TextInputEditText enterNewPass,enterReNewPass;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{4,}" +
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_new_password);

        enterReNewPass= findViewById(R.id.enterReNewPass);
        enterNewPass= findViewById(R.id.enterNewPass);
        buttonRepass= findViewById(R.id.buttonRepass);

        Intent intent = getIntent();
        id= intent.getStringExtra("mauser");

        buttonRepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = enterNewPass.getText().toString().trim();
                String reNewPass = enterReNewPass.getText().toString().trim();


                if (newPass.isEmpty() || reNewPass.isEmpty()){
                    CustomToast.makeText(getApplicationContext(),getString(R.string.vuilongnhapmatkhau), (int) CustomToast.SHORT,CustomToast.WARNING,true).show();
                }
                else if (!PASSWORD_PATTERN.matcher(newPass).matches()) {
                    enterNewPass.setError(getString(R.string.coktydbsochu));
                }else if (!newPass.equals(reNewPass)){
                    CustomToast.makeText(getApplicationContext(),getString(R.string.matkhkokhop), (int) CustomToast.SHORT,CustomToast.WARNING,true).show();
                }
                else {
                    ChangePassword();
                }
            }
        });
    }
    private void ChangePassword(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_CHANGE_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")){
                                CustomToast.makeText(getApplicationContext(),getString(R.string.thaydoithanhc), (int) CustomToast.SHORT,CustomToast.SUCCESS,true).show();
                                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                                startActivity(intent);
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
                params.put("id", id);
                params.put("password", enterNewPass.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
}
