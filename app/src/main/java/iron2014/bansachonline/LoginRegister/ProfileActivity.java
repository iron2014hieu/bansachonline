package iron2014.bansachonline.LoginRegister;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import iron2014.bansachonline.CustomToast;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.URL.EndPoints;
import iron2014.bansachonline.URL.UrlSql;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;

public class ProfileActivity extends AppCompatActivity {
    Dialog dialog;
    SharedPref sharedPref;
    TextView txtEmail, txtName, txtPhone, txtNgaySinh, txtSex, txtAddress;
    Button btnLogout, btnUpdateThongTin;
    SessionManager sessionManager;
    private String TAG = "TAG_PROFILE";
    private String token;
    private static String URL_EDIT ="https://bansachonline.xyz/bansach/loginregister/edit_detail.php";
    private static String URL_UPLOAD ="https://bansachonline.xyz/bansach/loginregister/upload.php";
    String email,strid,name,quyen,linh_img,phone, address, sex, ngaysinh, id;

    private Menu action;
    Bitmap bitmap;
    CircleImageView profile_image;
    Boolean replayedt = false;
    Toolbar toolbar1;

    EditText edtPhone;
    Button btnUpdate;
    ProgressBar progress_bar_changephone_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        theme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        toolbar1 = (Toolbar) findViewById(R.id.toolbar_profile_edit);


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        //Toobar đã như ActionBar

        sessionManager = new SessionManager( this);
        sessionManager.Checklogin();
        txtSex = findViewById(R.id.txtSex);
        txtPhone = findViewById(R.id.txtPhone);
        txtNgaySinh = findViewById(R.id.txtNgaySinh);
        txtEmail=findViewById(R.id.txtEmail);
        txtAddress = findViewById(R.id.txtAddress);
        txtName=findViewById(R.id.txtName);
        txtEmail=findViewById(R.id.txtEmail);
        btnLogout=findViewById(R.id.btnLogout);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){
                            token = task.getResult().getToken();
                        }
                    }
                });


        profile_image = findViewById(R.id.profile_image);

        HashMap<String,String> user = sessionManager.getUserDetail();
        email = user.get(sessionManager.EMAIL);
//        name = user.get(sessionManager.NAME);
//        phone = user.get(sessionManager.PHONE);
//        address = user.get(sessionManager.ADDRESS);
//        sex = user.get(sessionManager.SEX);
        id = user.get(sessionManager.ID);
//        ngaysinh = user.get(sessionManager.NGAYSINH);
//        quyen = user.get(sessionManager.QUYEN);

        getDetail(email);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.Logout();
                FirebaseAuth.getInstance().signOut();
                updateDevicesToken("0", id);
            }
        });

    }
public void doisdt(View view) {
        displayAlertDialog();
}
    // sưa sdt
        private void saveDetail(final String strid,  final String strphone) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_UDATE_PHONE ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String check = jsonObject.getString("check");

                            if (check.equals("chuatontai")) {
                                if (success.equals("1")) {
                                    CustomToast.makeText(getApplicationContext(), getString(R.string.suathanhcong), (int) CustomToast.SHORT, CustomToast.SUCCESS, true).show();
                                    recreate();
                                    dialog.dismiss();
                                }
                            }else {
                                CustomToast.makeText(getApplicationContext(), getString(R.string.sdtdatontai), (int) CustomToast.LONG, CustomToast.WARNING, true).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("printStackTrace", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: err", error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", strphone);
                params.put("id", strid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // phần xác minh
    public void displayAlertDialog() {
        // custom dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_chan_phone);
//        dialog.setTitle();

        // set the custom dialog components - text, image and button
        edtPhone = (EditText) dialog.findViewById(R.id.edtPhone);
        btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

        progress_bar_changephone_dialog = dialog.findViewById(R.id.progress_bar_changephone_dialog);

        final LinearLayout linearLayout_nhap =(LinearLayout) dialog.findViewById(R.id.linearLayoutdialog_Nhap);
        final RelativeLayout linearLayout_xacminh = dialog.findViewById(R.id.container_dialog);
        final ImageView img_close_phone = dialog.findViewById(R.id.img_close_phone);

        if (txtPhone.getText().toString()!=null){
            edtPhone.setText(txtPhone.getText().toString().trim());
        }
        img_close_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // if button is clicked, close the custom dialog
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number =edtPhone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    edtPhone.setError(getString(R.string.nhapsohl));
                    edtPhone.requestFocus();
                    return;
                }else {
                    saveDetail(id, number);
                }


            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getDetail(email);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void doimk(View view){
        startActivity(new Intent(getApplicationContext(), DoiMatkhauActivity.class));
    }
    @Override
    public void onBackPressed() {
        if (replayedt) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.banmuonhuy))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.co), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ProfileActivity.this.finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.khong), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }else
            ProfileActivity.this.finish();
    }
    public void getImage(View v){
        chooseFile();
    }
    private void getDetail(final String email1){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonArray = jsonobject.getJSONArray("users_table");
                            JSONObject data = jsonArray.getJSONObject(0);
                            int id = data.getInt("id");
                            name = data.getString("name");
                            email = data.getString("email");
                            address = data.getString("address");
                            phone = data.getString("phone");
                            ngaysinh = data.getString("ngaysinh");
                            sex = data.getString("sex");
                            String urlImage = data.getString("photo");
                            txtAddress.setText(address);
                            txtNgaySinh.setText(ngaysinh);
                            txtPhone.setText(phone);
                            txtSex.setText(sex);
                            txtEmail.setText(email);
                            txtName.setText(name);
//                            txtPassword.setText(password);
                            strid = String.valueOf(id);
                            Picasso.with(ProfileActivity.this).load(urlImage).into(profile_image) ;

                            toolbar1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("address", address);
                                    intent.putExtra("sex", sex);
                                    intent.putExtra("ngaysinh", ngaysinh);
                                    intent.putExtra("id", strid);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txtEmail.setText(error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email1);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            if (quyen.equals("user")){
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.menu_action, menu);
                action = menu;
                action.findItem(R.id.menu_save).setVisible(false);
            }else {
                getMenuInflater().inflate(R.menu.menu_action_admin, menu);
            }
        }catch (Exception e){
            Log.e("log", e.toString());
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            if (quyen.equals("user")){
                switch (item.getItemId()){
                    case R.id.menu_save:
                        saveDetail();
                      return  true;
                }
            }else {
                switch (item.getItemId()){
                    case R.id.ivQLTK:
                        break;
                }
            }
        }catch (Exception e){
            Log.e("log", e.toString());
        }
        return super.onOptionsItemSelected(item);
    }
    private void saveDetail(){
        final String name = txtName.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String id = strid;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(ProfileActivity.this, getString(R.string.luuthanhcog), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error saveProfile on: ", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: ", error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("id", strid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data !=null &&data.getData() != null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadPicture(strid, getStringImage(bitmap));
        }
    }

    private void uploadPicture(final String id, final String photo) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                params.put("photo", photo);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String endecodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return endecodeImage;
    }
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
    private void updateDevicesToken(final String islogin,final String mauser){

        if (token == null) {
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
                        Log.e("errupdateDevicesToken", error.getMessage());
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
