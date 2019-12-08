package iron2014.bansachonline.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import iron2014.bansachonline.CustomToast;
import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.URL.UrlSql;
import iron2014.bansachonline.fragmentVanChuyen.Activity.ShipperActivity;

public class VerifyPhoneActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText editText;
    boolean check =false;
    String code,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);


        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryAreaCodes));

        editText = findViewById(R.id.editTextPhone);

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

                number = editText.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editText.setError(getString(R.string.nhapsohl));
                    editText.requestFocus();
                    return;
                }else {
                    LoginWithphone(number);
//                    if (check == false){
//                        Toast.makeText(VerifyPhoneActivity.this, "Số "+number+" chưa được đăng ký!"+check, Toast.LENGTH_SHORT).show();
//                    }else {
//                        String phoneNumber = "+" + code + number;
//                        Toast.makeText(VerifyPhoneActivity.this, ""+check, Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(VerifyPhoneActivity.this, LoginWithSMSActivity.class);
//                        intent.putExtra("phonenumber", phoneNumber);
//                        intent.putExtra("sodienthoai", editText.getText().toString());
//                        startActivity(intent);
//                    }
                }



            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }
    private void LoginWithphone(final String phone){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlSql.URL_LOGIN_PHONE+phone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")){
                                String phoneNumber = "+" + code + number;
                                Intent intent = new Intent(VerifyPhoneActivity.this, LoginWithSMSActivity.class);
                                intent.putExtra("phonenumber", phoneNumber);
                                intent.putExtra("sodienthoai", editText.getText().toString());
                                startActivity(intent);
                            }else {
                                CustomToast.makeText(getApplicationContext(),getString(R.string.so) +" "+number+" "+ getString(R.string.chuaduocdk), (int) CustomToast.LONG,CustomToast.WARNING,true).show();
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
}
