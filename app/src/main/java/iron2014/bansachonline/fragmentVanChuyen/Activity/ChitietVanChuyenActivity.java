package iron2014.bansachonline.fragmentVanChuyen.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import iron2014.bansachonline.R;
import iron2014.bansachonline.adapter.hoadoncthd.CTHDAdapter;
import iron2014.bansachonline.model.CTHD;
import retrofit2.Call;
import retrofit2.Callback;

public class ChitietVanChuyenActivity extends AppCompatActivity {

    TextView tvMaHD, tvTenKH, tvDiaChi, tvSDT, tvTongTien, tvTinhTrang;
    Button btnNhanhang;
    RecyclerView recyclerView;
    List<CTHD> cthdList = new ArrayList<>();
    CTHDAdapter cthdAdapter;
    ApiInTerFaceHoadon apiInTerFaceHoadon;
    public static String mahd, tenkh, diachi, sdt, tongtien, tinhtrang;

    String URL_UDATE = "https://bansachonline.xyz/bansach/hoadon/update_hoadon_tinhtrang.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_van_chuyen);
        recyclerView = findViewById(R.id.recyclerview_xacnhadonhang);

        btnNhanhang = findViewById(R.id.btnNhanhang);

        tvMaHD = findViewById(R.id.tvmaHD);
        tvTenKH = findViewById(R.id.tvtenKH);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvSDT = findViewById(R.id.tvSDT);
        tvTongTien = findViewById(R.id.tvTongTien);
        tvTinhTrang = findViewById(R.id.tvTinhTrang);
        Intent intent = getIntent();
        mahd = intent.getStringExtra("mahoadon");
        tenkh = intent.getStringExtra("tenkh");
        diachi = intent.getStringExtra("diachi");
        sdt = intent.getStringExtra("sdt");
        tongtien = intent.getStringExtra("tongtien");
        tinhtrang = intent.getStringExtra("tinhtrang");
        if (tinhtrang!=null&&tinhtrang.equals("danhgia")){
            btnNhanhang.setVisibility(View.GONE);
        }
        tvTenKH.setText(tenkh);
        tvDiaChi.setText(diachi);
        tvSDT.setText(sdt);
        tvTongTien.setText(tongtien);
        tvTinhTrang.setText(tinhtrang);

        cthdAdapter = new CTHDAdapter(this, cthdList);

        StaggeredGridLayoutManager gridLayoutManagerVeticl =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManagerVeticl);
        recyclerView.setHasFixedSize(true);

        fetchcthdbymahd(mahd);
        if (tinhtrang.equals("choxacnhan")){
            btnNhanhang.setVisibility(View.GONE);
        }

        btnNhanhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tinhtrang.equals("cholayhang")) {
                    UpdateTinhtrang( "danggiao", URL_UDATE);
                }

            }
        });
    }
    public void fetchcthdbymahd(String mahd){
        apiInTerFaceHoadon = ApiClient.getApiClient().create(ApiInTerFaceHoadon.class);
        Call<List<CTHD>> call = apiInTerFaceHoadon.get_cthd_bymahd(mahd);

        call.enqueue(new Callback<List<CTHD>>() {
            @Override
            public void onResponse(Call<List<CTHD>> call, retrofit2.Response<List<CTHD>> response) {
                //progressBar.setVisibility(View.GONE);
                cthdList= response.body();
                cthdAdapter = new CTHDAdapter(ChitietVanChuyenActivity.this,cthdList);
                recyclerView.setAdapter(cthdAdapter);
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
                        Toast.makeText(ChitietVanChuyenActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        if (response.equals("tc")){
                            Toast.makeText(ChitietVanChuyenActivity.this, "tc", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(), ShipperActivity.class);
                            intent.putExtra("check", 1);
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
}
