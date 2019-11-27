package iron2014.bansachonline.Activity.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceHoadon;
import iron2014.bansachonline.MuahangActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.adapter.hoadoncthd.CTHDAdapter;
import iron2014.bansachonline.model.CTHD;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;
import retrofit2.Call;
import retrofit2.Callback;

public class ChitiethoadonActivity extends AppCompatActivity {
    LinearLayout lnThongtin;
    TextView tvTenkhachhang, tvdiachiKh, tvsdtKH, tvtinhtrangsp;
    RecyclerView recyclerView_cthd;
    Button btnXacNhanHang;
    List<CTHD> cthdList = new ArrayList<>();
    CTHDAdapter cthdAdapter;
    ApiInTerFaceHoadon apiInTerFaceHoadon;
    public static String mahd, tinhtrang, tenkh, diachi, sdt;
    SharedPref sharedPref;
    String URL_UDATE = "https://bansachonline.xyz/bansach/hoadon/update_hoadon_tinhtrang.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_chitiethoadon);
        Anhxa();
        Intent intent = getIntent();
        mahd = intent.getStringExtra("mahd");
        tinhtrang = intent.getStringExtra("tinhtrang");
        tenkh = intent.getStringExtra("tenkh");
        diachi = intent.getStringExtra("diachi");
        sdt = intent.getStringExtra("sdt");
        cthdAdapter = new CTHDAdapter(this, cthdList);

        StaggeredGridLayoutManager gridLayoutManagerVeticl =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_cthd.setLayoutManager(gridLayoutManagerVeticl);
        recyclerView_cthd.setHasFixedSize(true);

        tvTenkhachhang = findViewById(R.id.tvTenkhachhang);
        tvdiachiKh = findViewById(R.id.tvdiachiKh);
        tvsdtKH = findViewById(R.id.tvsdtKH);
        tvtinhtrangsp = findViewById(R.id.tvtinhtrangsp);
        tvtinhtrangsp.setText(tinhtrang);
        tvTenkhachhang.setText(tenkh);
        tvdiachiKh.setText(diachi);
        tvsdtKH.setText(sdt);

        lnThongtin = findViewById(R.id.lnThongtin);

        if(tinhtrang!=null&&tinhtrang.equals("choxacnhan")){
            tvtinhtrangsp.setText("Chờ xác nhận");
            lnThongtin.setVisibility(View.VISIBLE);
        }
        btnXacNhanHang = findViewById(R.id.btnXacNhanHang);
        btnXacNhanHang.setVisibility(View.GONE);
        if (tinhtrang!=null&&tinhtrang.equals("userxacnhan")){
            btnXacNhanHang.setVisibility(View.VISIBLE);
            lnThongtin.setVisibility(View.VISIBLE);
            tvtinhtrangsp.setText("Nhận đơn hàng");
        }else if (tinhtrang!=null&&tinhtrang.equals("danggiao")){
            btnXacNhanHang.setVisibility(View.GONE);
            lnThongtin.setVisibility(View.VISIBLE);
            tvtinhtrangsp.setText("Đang vận chuyển");
        }else if (tinhtrang!=null&&tinhtrang.equals("danhgia")){
            btnXacNhanHang.setVisibility(View.GONE);
            tvtinhtrangsp.setText("Đã nhận hàng");
        }

        btnXacNhanHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tinhtrang.equals("userxacnhan")) {
                    UpdateTinhtrang( "danhgia", URL_UDATE);
                }
            }
        });
        btnXacNhanHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tinhtrang.equals("userxacnhan")) {
                    UpdateTinhtrang( "danhgia", URL_UDATE);
                }
            }
        });
        fetchcthdbymahd(mahd);
    }
    //settheme
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
    public void fetchcthdbymahd(String mahd){
        apiInTerFaceHoadon = ApiClient.getApiClient().create(ApiInTerFaceHoadon.class);
        Call<List<CTHD>> call = apiInTerFaceHoadon.get_cthd_bymahd(mahd);

        call.enqueue(new Callback<List<CTHD>>() {
            @Override
            public void onResponse(Call<List<CTHD>> call, retrofit2.Response<List<CTHD>> response) {
                //progressBar.setVisibility(View.GONE);
                cthdList= response.body();
                cthdAdapter = new CTHDAdapter(ChitiethoadonActivity.this,cthdList);
                recyclerView_cthd.setAdapter(cthdAdapter);
                cthdAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CTHD>> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    private void UpdateTinhtrang(final String tinhtrang1 ,String url){
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ChitiethoadonActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        if (response.equals("tc")){
                            Toast.makeText(ChitiethoadonActivity.this, "tc", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(), MuahangActivity.class);
                            intent.putExtra("check", 3);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("update tt er ", error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tinhtrang", tinhtrang1);
                params.put("mahoadon", mahd);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private void Anhxa(){
        recyclerView_cthd = findViewById(R.id.recyclerview_cthd);
    }
}
