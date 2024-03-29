package iron2014.bansachonline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.widget.NestedScrollView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import iron2014.bansachonline.Activity.CartListActivity;
import iron2014.bansachonline.Activity.ChonMaKhuyenmaiActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceDatmua;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceHoadon;
import iron2014.bansachonline.LoginRegister.CountryData;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.URL.UrlSql;
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
    private List<KhuyenMai> listKhuyenMai = new ArrayList<>();
    ApiInTerFaceHoadon apiInTerFaceHoadon;

    private String verificationId;
    EditText edtMaGiamGia, edtSdt, edtDiachi, edtTenkh,  edtEnterPhone,editTextCode_dialog;
    TextView txtTongtien, txtTiensanpham, txtTienvanchuyen, txtTongthanhtoan;
    Button btnCheckMGG,btnThanhtoan  ,btnTieptuc;
    RecyclerView recyclerview_create_bill;
    List<DatMua> listDatmua = new ArrayList<>();
    ListSPAdapter cartAdapter;
    ApiInTerFaceDatmua apiInTerFaceDatmua;
    TextView TxtTienKhuyenmai;

    String mauser_session;
    Dialog dialog;
    Button buttonsuccess_dialog;
    ProgressBar progress_bar_dialog;

    int sizeList=0;
    SessionManager sessionManager;
    ProgressBar progress_hoadon;
    String phoneNumber;

    Double tongtien = 0.0;
    Double trukhuyenmai =0.0;
    Double tienvanchuyen =0.0;

    Double tienthanhtoan = 0.0;

    SharedPref sharedPref;
    private String tenkh,diachi,sodienthoai;
    private NotificationManagerCompat notificationManager;
    NestedScrollView scroller;
    String TAG ="muahang";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_cart_detail);
        addControls();


        sessionManager = new SessionManager(this);
        notificationManager = NotificationManagerCompat.from(this);
        mAuth = FirebaseAuth.getInstance();
        HashMap<String,String> user = sessionManager.getUserDetail();
        mauser_session = user.get(sessionManager.ID);
        String email = user.get(sessionManager.EMAIL);


        getDetail(email);


        txtTienvanchuyen = findViewById(R.id.txtTienvanchuyen);
        txtTiensanpham = findViewById(R.id.txtTiensanpham);
        txtTongthanhtoan = findViewById(R.id.txtTongthanhtoan);

        Intent intent = getIntent();
        HashMap<String,String> dathang = sessionManager.getTongtien();
        tongtien = Double.valueOf(dathang.get(sessionManager.TONGTIEN));

        //        tenkh = user.get(sessionManager.NAME);
//        diachi = user.get(sessionManager.ADDRESS);
//        sodienthoai = user.get(sessionManager.PHONE);
        tienthanhtoan = tongtien+trukhuyenmai+tienvanchuyen;
        txtTongthanhtoan.setText(String.valueOf(tienthanhtoan));

        Toolbar toolbar = findViewById(R.id.toolbargh);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        try {
            edtDiachi.setText(diachi);
            edtSdt.setText(sodienthoai);
        }catch (Exception e){

        }
        edtTenkh.setText(tenkh);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });


        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();


        txtTiensanpham.setText(tongtien+" VNĐ");
        TxtTienKhuyenmai.setText(trukhuyenmai+" VNĐ");
        txtTienvanchuyen.setText(tienvanchuyen+" VNĐ");


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


                tienvanchuyen = Double.valueOf(clickItemPrice);

                tienthanhtoan = tongtien+trukhuyenmai+tienvanchuyen;

                txtTongthanhtoan.setText(String.valueOf(tienthanhtoan));

                txtTiensanpham.setText(tongtien+" VNĐ");
                TxtTienKhuyenmai.setText(trukhuyenmai+" VNĐ");
                txtTienvanchuyen.setText(tienvanchuyen+" VNĐ");

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        edtMaGiamGia.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChonMaKhuyenmaiActivity.class));
                CustomToast.makeText(getApplicationContext(),getString(R.string.haychonmacan), (int) CustomToast.SHORT,CustomToast.WARNING,true).show();
                return false;
            }

        });
        StaggeredGridLayoutManager gridLayoutManager3 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
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
                    CustomToast.makeText(getApplicationContext(),getString(R.string.khongcokm), (int) CustomToast.SHORT,CustomToast.CONFUSING,true).show(); } else {
                    btnCheckMGG.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (int i = 0; i < listKhuyenMai.size(); i++) {
                                String makm = listKhuyenMai.get(i).getMaKM();
                                Double phantram = Double.valueOf(listKhuyenMai.get(i).getTinhChat());
                                String s = edtMaGiamGia.getText().toString();
                                if (!s.equals("")) {
                                    if (makm.equals(s)) {
                                        CustomToast.makeText(getApplicationContext(),getString(R.string.hople), (int) CustomToast.SHORT,CustomToast.SUCCESS,true).show();

                                        trukhuyenmai = (tongtien*phantram) - tongtien;
                                        tienthanhtoan = tongtien+trukhuyenmai+tienvanchuyen;

                                        txtTongthanhtoan.setText(String.valueOf(tienthanhtoan));

                                        txtTiensanpham.setText(tongtien+" VNĐ");
                                        TxtTienKhuyenmai.setText(trukhuyenmai+" VNĐ");
                                        txtTienvanchuyen.setText(tienvanchuyen+" VNĐ");
                                        break;
                                    }
//                                    else {
//                                        Toast.makeText(CartDetailActivity.this, getString(R.string.maktt), Toast.LENGTH_SHORT).show();
//                                    }

                                }
                                else {
                                    CustomToast.makeText(getApplicationContext(),getString(R.string.banchunhapma), (int) CustomToast.SHORT,CustomToast.CONFUSING,true).show();break;
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
                if (edtTenkh.getText().toString().isEmpty()){
                    CustomToast.makeText(getApplicationContext(),getString(R.string.nhaptenkh), (int) CustomToast.SHORT,CustomToast.WARNING,true).show();
                }else if (edtDiachi.getText().toString().isEmpty()){
                    CustomToast.makeText(getApplicationContext(),getString(R.string.nhapdc), (int) CustomToast.SHORT,CustomToast.WARNING,true).show();
                }else if (edtSdt.getText().toString().isEmpty()){
                    CustomToast.makeText(getApplicationContext(),getString(R.string.nhapsodient), (int) CustomToast.SHORT,CustomToast.WARNING,true).show(); }else {
                    AlertDialog.Builder alertDialog = new  AlertDialog.Builder(CartDetailActivity.this);
                    alertDialog.setMessage(getString(R.string.bancomuontt));
                    alertDialog.setIcon(R.drawable.ic_check_black_24dp);
                    alertDialog.setTitle(getString(R.string.dathang));
                    alertDialog.setPositiveButton(getString(R.string.co), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            displayAlertDialog();
                            //ThemHoadon(mauser_session, String.valueOf(tongtien), edtSdt.getText().toString(), UrlSql.url_insert_hoadon);
                        }
                    });
                    alertDialog.setNegativeButton(getString(R.string.khong), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progress_hoadon.setVisibility(View.GONE);
                            btnThanhtoan.setVisibility(View.VISIBLE);
                        }

                    });
                    alertDialog.show();
                }
            }
        });

        if (scroller != null) {

            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i(TAG, "Scroll DOWN");
                    }
                    if (scrollY < oldScrollY) {
                        Log.i(TAG, "Scroll UP");
                    }

                    if (scrollY == 0) {
                        Log.i(TAG, "TOP SCROLL");
                    }

                    if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                        Log.i(TAG, "BOTTOM SCROLL");
                    }
                }
            });
        }
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
                            tenkh = data.getString("name");
                            diachi = data.getString("address");
                            sodienthoai = data.getString("phone");

                            edtDiachi.setText(diachi);
                            edtSdt.setText(sodienthoai);
                            edtTenkh.setText(tenkh);


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
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email1);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //settheme
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        try {
            CharSequence textToPaste = clipboard.getPrimaryClip().getItemAt(0).getText();
            edtMaGiamGia.setText(textToPaste);
        } catch (Exception e) {
            return;
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), CartListActivity.class));
    }

    private void initList(){
        countryItems = new ArrayList<>();
        countryItems.add(new CountryItem(getString(R.string.giaohangtk),"20000", R.drawable.vanchuyen));
        countryItems.add(new CountryItem(getString(R.string.giaohangnh), "30000", R.drawable.vanchuyen));
        countryItems.add(new CountryItem(getString(R.string.ghst), "45000", R.drawable.vanchuyen));
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
                                int id = (listDatmua.get(n).getId());

                                ThemCTHD(String.valueOf(mahoadon), masach, tensach, giaban, soluong, hinhanh, UrlSql.url_insert_cthd);
                                UpdateSoluong(masach, soluong);
                                xoaGiohang(String.valueOf(id),mauser);
                            }
                            sendOnChannel(getString(R.string.hod)+" "+mahoadon,getString(R.string.xemdhb));
                            String mota = getString(R.string.dh)+" "+mahoadon +" "+getString(R.string.choxl);
                            InsertNotif(mota,String.valueOf(mahoadon));
                            CustomToast.makeText(getApplicationContext(),getString(R.string.yhemtchd) +" "+ mahoadon, (int) CustomToast.SHORT,CustomToast.SUCCESS,true).show(); } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

//                            startActivity(new Intent(getBaseContext(), CartDetailActivity.class));
                            Intent intent = new Intent(getBaseContext(), MuahangActivity.class);
                            intent.putExtra("check", 0);

                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
    private void xoaGiohang( final String iddatmua, final String mauser) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_DELETE_GIOHANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("tc")){

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MYSQL", "Lỗi!" +error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("id", iddatmua);
                params.put("mauser", mauser);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void UpdateSoluong( final String masach, final String soluong) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_UPDATE_SOLUONG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MYSQL", "Lỗi! \n" +error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("masach", masach);
                params.put("soluong", soluong);
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
                .setSmallIcon(R.drawable.book)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(title + " " + message)
                        .setBigContentTitle(getString(R.string.donhangdangducsl))
                        .setSummaryText(getString(R.string.adumakhe)))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup("")
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
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_xacminh_hoadon);
//        dialog.setTitle();

        // set the custom dialog components - text, image and button
         edtEnterPhone = (EditText) dialog.findViewById(R.id.editTextPhone_dialog);
         btnTieptuc = (Button) dialog.findViewById(R.id.btnxacminhsdt_dialog);
        editTextCode_dialog =(EditText)dialog.findViewById(R.id.editTextCode_dialog);
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner_dialog_Countries);
        buttonsuccess_dialog = dialog.findViewById(R.id.buttonsuccess_dialog);
        final TextView txtVuilongdoi =(TextView) dialog.findViewById(R.id.textView_dialog);
        progress_bar_dialog = dialog.findViewById(R.id.progress_bar_dialog);

        final LinearLayout linearLayout_nhap =(LinearLayout) dialog.findViewById(R.id.linearLayoutdialog_Nhap);
        final RelativeLayout linearLayout_xacminh = dialog.findViewById(R.id.container_dialog);
        final ImageView img_close = dialog.findViewById(R.id.img_close);
        final ImageView img_close_2 = dialog.findViewById(R.id.img_close_2);


        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryAreaCodes));

        if (sodienthoai!=null){
            edtEnterPhone.setText(sodienthoai);
        }
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        img_close_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // if button is clicked, close the custom dialog
        btnTieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number =edtEnterPhone.getText().toString().trim();;

                if (number.isEmpty() || number.length() < 10) {
                    edtEnterPhone.setError(getString(R.string.nhapsohl));
                    edtEnterPhone.requestFocus();
                    return;
                }else {
                    phoneNumber = "+" + code + number;
                    linearLayout_nhap.setVisibility(View.GONE);
                    linearLayout_xacminh.setVisibility(View.VISIBLE);
                    sendVerificationCode(phoneNumber);
                }


            }
        });
        buttonsuccess_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode_dialog.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editTextCode_dialog.setError("Mã đang trống hoặc mã phải có 6 ký tự");
                    editTextCode_dialog.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                            buttonsuccess_dialog.setVisibility(View.GONE);
                            editTextCode_dialog.setVisibility(View.GONE);
                            progress_bar_dialog.setVisibility(View.VISIBLE);

                            ThemHoadon(mauser_session, String.valueOf(tienthanhtoan), edtSdt.getText().toString(), UrlSql.url_insert_hoadon);
                        }else {
                            CustomToast.makeText(getApplicationContext(),getString(R.string.nhapdaydutt), (int) CustomToast.SHORT,CustomToast.WARNING,true).show();
                        }
                        } else {
                        }
                    }
                });
    }
    private void sendVerificationCode(String number) {
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
        }
    };
    private void InsertNotif (final String mota, final String mahoadon){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_INSERT_NOTIF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tieude", getString(R.string.choxuly));
                params.put("mota", mota);
                params.put("mahoadon", mahoadon);
                params.put("mauser", mauser_session);
                params.put("loaithongbao", "donhang");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addControls() {
        progress_hoadon=findViewById(R.id.progress_hoadon);
        edtMaGiamGia = findViewById(R.id.edtMaGiamGia);
        txtTongtien= findViewById(R.id.txtTongtienthanhtoan);
        edtSdt = findViewById(R.id.edtSdt);
        edtDiachi=findViewById(R.id.edtDiachi);
        edtTenkh=findViewById(R.id.edtTenkh);
        btnCheckMGG = findViewById(R.id.CheckMGG);
        btnThanhtoan= findViewById(R.id.btnThanhtoan);
        TxtTienKhuyenmai=findViewById(R.id.TxtTienKhuyenmai);
        recyclerview_create_bill= findViewById(R.id.recyclerview_create_bill);
        scroller= findViewById(R.id.nestedScrollView);
    }
}
