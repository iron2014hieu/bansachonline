package iron2014.bansachonline.Activity;

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

import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;

public class TheloaiChitietActivity extends AppCompatActivity {
    private EditText editTextTentl, editTextMotatl;
    private Button buttonCapnhat;
    private String URL_UPDATE_THELOAI = "https://hieuttpk808.000webhostapp.com/books/theloai/update.php";
    private String TAG = TheloaiChitietActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theloai_chitiet);
        addcontrols();
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String name = b.getString("TEN");
        String mota = b.getString("MOTA");
        final int id = b.getInt("ID");

        setTitle("Cập nhật thể loại "+name);
        editTextTentl.setText(name);
        editTextMotatl.setText(mota);

        buttonCapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updeteTheloai(String.valueOf(id));
            }
        });
    }
    private void addcontrols(){
        buttonCapnhat=findViewById(R.id.btnCapnhat);
        editTextTentl = findViewById(R.id.edtTentheloai);
        editTextMotatl=findViewById(R.id.edtMotatheloai);
    }
    private void updeteTheloai(final String id) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_THELOAI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                progressDialog.dismiss();
                                Toast.makeText(TheloaiChitietActivity.this, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(TheloaiChitietActivity.this, "Loi e! "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(TheloaiChitietActivity.this, "Error ! "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                params.put("tentheloai", editTextTentl.getText().toString().trim());
                params.put("mota", editTextMotatl.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
