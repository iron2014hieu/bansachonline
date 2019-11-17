package iron2014.bansachonline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceDatmua;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceHoadon;
import iron2014.bansachonline.LoginRegister.CountryData;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.KhuyenMai.KhuyenMaiAdapter;
import iron2014.bansachonline.adapter.ListSPAdapter;
import iron2014.bansachonline.model.DatMua;
import iron2014.bansachonline.model.KhuyenMai;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;
import iron2014.bansachonline.nighmode_vanchuyen.vanchuyen.CountryAdapter;
import iron2014.bansachonline.nighmode_vanchuyen.vanchuyen.CountryItem;
import retrofit2.Call;
import retrofit2.Callback;

import static iron2014.bansachonline.Notif.App.CHANNEL_1_ID;


public class CartDetailActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ArrayList<CountryItem> countryItems;
    private CountryAdapter countryAdapter;
    private KhuyenMaiAdapter khuyenMaiAdapter;
    private List<KhuyenMai> listKhuyenMai = new ArrayList<>();
    ApiInTerFaceHoadon apiInTerFaceHoadon;
    ProgressBar progressBar;
    private String verificationId;
    EditText edtMaGiamGia, edtSdt, edtDiachi, edtTenkh,  edtEnterPhone,editTextCode_dialog;
    TextView txtTongtien;
    Button btnCheckMGG,btnThanhtoan  ,btnTieptuc;
    RecyclerView recyclerview_create_bill;
    List<DatMua> listDatmua = new ArrayList<>();
    ListSPAdapter cartAdapter;
    ApiInTerFaceDatmua apiInTerFaceDatmua;
    int tongtien;
    String url_insert_cthd="https://bansachonline.xyz/bansach/hoadon/them_cthd.php",mauser_session;
    String url_insert_hoadon ="https://bansachonline.xyz/bansach/hoadon/them_hoadon.php";
    int sizeList=0;
    SessionManager sessionManager;
    ProgressBar progress_hoadon;
    String phoneNumber;
    int Giamgia = 0;
    int Giatri;
    int Phivanchuyen = 0;
    SharedPref sharedPref;
    private NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_cart_detail);
        progress_hoadon=findViewById(R.id.progress_hoadon);
        edtMaGiamGia = findViewById(R.id.edtMaGiamGia);
        txtTongtien= findViewById(R.id.txtTongtienthanhtoan);
        edtSdt = findViewById(R.id.edtSdt);
        edtDiachi=findViewById(R.id.edtDiachi);
        edtTenkh=findViewById(R.id.edtTenkh);
        btnCheckMGG = findViewById(R.id.CheckMGG);
        btnThanhtoan= findViewById(R.id.btnThanhtoan);

        recyclerview_create_bill= findViewById(R.id.recyclerview_create_bill);
        sessionManager = new SessionManager(this);
        notificationManager = NotificationManagerCompat.from(this);
        mAuth = FirebaseAuth.getInstance();

        HashMap<String,String> user = sessionManager.getUserDetail();
        mauser_session = user.get(sessionManager.ID);
        Intent intent = getIntent();
        tongtien = Integer.valueOf(intent.getStringExtra("tongtien"));

        Toolbar toolbar = findViewById(R.id.toolbargh);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();

        Giatri = Integer.valueOf(tongtien);
        if (Giatri > 100000){
            Giatri -= Giatri * 0.05 ;
            txtTongtien.setText(String.valueOf(Giatri));
        }else if(Giatri>250000) {
            Toast.makeText(this, "free ship", Toast.LENGTH_SHORT).show();
        }

        txtTongtien.setText(tongtien+ " VNĐ");

        cartAdapter = new ListSPAdapter(this,listDatmua);

        initList();
        Spinner spinner = findViewById(R.id.spinner_countries);
        countryAdapter = new CountryAdapter(this, countryItems);
        spinner.setAdapter(countryAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CountryItem clickItem = (CountryItem) adapterView.getItemAtPosition(i);
                String clickItemContry = clickItem.getCountryName();
                String clickItemPrice = clickItem.getPrice();
                int tt = Integer.valueOf(tongtien);
                tt+= Integer.valueOf(clickItemPrice);
                tt-= Giamgia;
                Phivanchuyen = Integer.valueOf(clickItemPrice);
                txtTongtien.setText(tt+ " VNĐ");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        StaggeredGridLayoutManager gridLayoutManager3 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerview_create_bill.setLayoutManager(gridLayoutManager3);
        recyclerview_create_bill.setHasFixedSize(true);
        fetchTacgia(mauser_session);
        progress_hoadon.setVisibility(View.GONE);

        apiInTerFaceHoadon = ApiClient.getApiClient().create(ApiInTerFaceHoadon.class);
        Call<List<KhuyenMai>> call = apiInTerFaceHoadon.get_all_khuyenmai();
        //check khuyến mãi
        call.enqueue(new Callback<List<KhuyenMai>>() {
            @Override
            public void onResponse(Call<List<KhuyenMai>> call, retrofit2.Response<List<KhuyenMai>> response) {
                listKhuyenMai = response.body();
                if (listKhuyenMai.size() == 0) {
                    Toast.makeText(CartDetailActivity.this, "Không có khuyến mãi", Toast.LENGTH_SHORT).show();
                } else {
                    btnCheckMGG.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (int i = 0; i < listKhuyenMai.size(); i++) {
                                String makm = listKhuyenMai.get(i).getMaKM();
                                String phantram = listKhuyenMai.get(i).getTinhChat();
                                String s = edtMaGiamGia.getText().toString();
                                if (!s.equals("")) {
                                    if (makm.equals(s)) {
                                        Toast.makeText(CartDetailActivity.this, "Hợp lệ " + makm, Toast.LENGTH_SHORT).show();
                                        Double tt = tongtien * Double.valueOf(phantram) + Phivanchuyen;
                                        txtTongtien.setText(tt + " VNĐ");
                                        break;
                                    } else {
                                        Toast.makeText(CartDetailActivity.this, "Mã không tồn tại", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                else {
                                    Toast.makeText(CartDetailActivity.this, "Bạn chưa nhập mã", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<KhuyenMai>> call, Throwable t) {
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
        btnThanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new  AlertDialog.Builder(CartDetailActivity.this);
                alertDialog.setMessage("Bạn có muốn thanh toán đơn hàng này không");
                alertDialog.setIcon(R.drawable.ic_check_black_24dp);
                alertDialog.setTitle("Đặt hàng");
                alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        displayAlertDialog();
                    }
                });
                alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progress_hoadon.setVisibility(View.GONE);
                        btnThanhtoan.setVisibility(View.VISIBLE);
                    }

                });
                alertDialog.show();
            }
        });
    }

    //settheme
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
    private void initList(){
        countryItems = new ArrayList<>();
        countryItems.add(new CountryItem("Giao hàng tiết kiệm","20000", R.drawable.vanchuyen));
        countryItems.add(new CountryItem("Giao hàng nhanh", "30000", R.drawable.vanchuyen));
        countryItems.add(new CountryItem("Giao hàng siêu tốc", "45000", R.drawable.vanchuyen));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_thanhtoan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.iv_khuyenmai:
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("check","2");
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchTacgia(String mauser){
        apiInTerFaceDatmua = ApiClient.getApiClient().create(ApiInTerFaceDatmua.class);
        Call<List<DatMua>> call = apiInTerFaceDatmua.getDatMuaThanhtoan(mauser);

        call.enqueue(new Callback<List<DatMua>>() {
            @Override
            public void onResponse(Call<List<DatMua>> call, retrofit2.Response<List<DatMua>> response) {
                progress_hoadon.setVisibility(View.GONE);
                listDatmua= response.body();
                sizeList = listDatmua.size();
                cartAdapter = new ListSPAdapter(CartDetailActivity.this,listDatmua);
                recyclerview_create_bill.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<DatMua>> call, Throwable t) {
                progress_hoadon.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    private void ThemHoadon( final String mauser, final String tongtien,final String sdt,String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int mahoadon =Integer.valueOf(jsonObject.getString("mahoadon"));
                            mahoadon++;
                            for (int n = 0; n < sizeList; n++) {
                                String masach = String.valueOf(listDatmua.get(n).getMasach());
                                String tensach = listDatmua.get(n).getSanpham();
                                String giaban = String.valueOf(listDatmua.get(n).getGia());
                                String soluong = String.valueOf(listDatmua.get(n).getSoluong());
                                String hinhanh = listDatmua.get(n).getHinhanh();
                                ThemCTHD(String.valueOf(mahoadon), masach, tensach, giaban, soluong, hinhanh, url_insert_cthd);
                            }
                            Toast.makeText(CartDetailActivity.this, "Thêm thành công hóa đơn: " + mahoadon, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                Map<String, String > params = new HashMap<>();
                params.put("mauser", mauser);
                params.put("tongtien", tongtien);
                params.put("tenkh", edtTenkh.getText().toString().trim());
                params.put("diachi", edtDiachi.getText().toString().trim());
                params.put("sdt", sdt);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void ThemCTHD( final String mahd, final String masach, final String tensach, final String giaban,
                           final String soluong, final String hinhanh,
                           String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("tc")){
                            progress_hoadon.setVisibility(View.GONE);
                            btnThanhtoan.setVisibility(View.VISIBLE);
                            sendOnChannel("Hóa đơn: "+mahd,"Xem đơn hàng của bạn");
//                            startActivity(new Intent(getBaseContext(), CartDetailActivity.class));
                            Intent intent = new Intent(getBaseContext(), MuahangActivity.class);
                            intent.putExtra("check", 0);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Loi roi nhe", Toast.LENGTH_SHORT).show();
                Log.d("MYSQL", "Lỗi! \n" +error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("mahd", mahd);
                params.put("masach", masach);
                params.put("tensach", tensach);
                params.put("giaban", giaban);
                params.put("soluong", soluong);
                params.put("hinhanh", hinhanh);
                params.put("mauser", mauser_session);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void sendOnChannel(String title, String message) {
        Intent activityIntent = new Intent(this, MuahangActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Notification summaryNotification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_arrow)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(title + " " + message)
                        .setBigContentTitle("1 đơn hàng mới")
                        .setSummaryText("user@example.com"))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup("example_group")
                .setAutoCancel(true)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setContentIntent(contentIntent)
                .setGroupSummary(true)
                .build();
        summaryNotification.defaults |= Notification.DEFAULT_SOUND;
        SystemClock.sleep(1000);
        notificationManager.notify(2, summaryNotification);
    }

    // phần xác minh
    public void displayAlertDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_xacminh_hoadon);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
         edtEnterPhone = (EditText) dialog.findViewById(R.id.editTextPhone_dialog);
         btnTieptuc = (Button) dialog.findViewById(R.id.btnxacminhsdt_dialog);
        editTextCode_dialog =(EditText)dialog.findViewById(R.id.editTextCode_dialog);
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner_dialog_Countries);
        final Button btnHoanthanh = dialog.findViewById(R.id.buttonsuccess_dialog);
         progressBar =(ProgressBar) dialog.findViewById(R.id.progressbar_dialog);
        final TextView txtVuilongdoi =(TextView) dialog.findViewById(R.id.textView_dialog);

        final LinearLayout linearLayout_nhap =(LinearLayout) dialog.findViewById(R.id.linearLayoutdialog_Nhap);
        final LinearLayout linearLayout_xacminh =(LinearLayout) dialog.findViewById(R.id.container_dialog);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryAreaCodes));

        // if button is clicked, close the custom dialog
        btnTieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

                String number = edtEnterPhone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    edtEnterPhone.setError("Vui lòng nhập số hợp lệ");
                    edtEnterPhone.requestFocus();
                    return;
                }

                phoneNumber = "+" + code + number;
                linearLayout_nhap.setVisibility(View.GONE);
                linearLayout_xacminh.setVisibility(View.VISIBLE);
                sendVerificationCode(phoneNumber);
            }
        });
        btnHoanthanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode_dialog.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editTextCode_dialog.setError("Enter code...");
                    editTextCode_dialog.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
        dialog.show();
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
                        if (!edtDiachi.getText().toString().trim().equals("")||
                        !edtSdt.getText().toString().trim().equals("") || !edtTenkh.getText().toString().trim().equals("")) {
                            progress_hoadon.setVisibility(View.VISIBLE);

                            ThemHoadon(mauser_session, String.valueOf(tongtien), edtSdt.getText().toString(), url_insert_hoadon);
                        }else {
                            Toast.makeText(CartDetailActivity.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        }
                        } else {
                            Toast.makeText(CartDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
                editTextCode_dialog.setText(code);
                verifyCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(CartDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
