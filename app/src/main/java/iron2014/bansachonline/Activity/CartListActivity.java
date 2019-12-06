package iron2014.bansachonline.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceDatmua;
import iron2014.bansachonline.CartDetailActivity;
import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.CartAdapter;
import iron2014.bansachonline.model.DatMua;
import retrofit2.Call;
import retrofit2.Callback;

public class CartListActivity extends AppCompatActivity {
    TextView tvMuatiep, tv_tongthanhtoan, txtDonvi, tvLaymaKM,txtnull_list;

    ApiInTerFaceDatmua apiInTerFaceDatmua;
    CartAdapter cartAdapter;
    RecyclerView recyclerView_dat_mua;
    private List<DatMua> listDatmua = new ArrayList<>();
    int sizeList;
    public  static TextView txtTongtien;
    private Button btnnext;
    String quyen, name, idUser;
    SessionManager sessionManager;
    public static int total=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        tv_tongthanhtoan = findViewById(R.id.tv_tongthanhtoan);
        txtDonvi = findViewById(R.id.txtDonvi);
        tvMuatiep = findViewById(R.id.tvMuatiep);

        Toolbar toolbar = findViewById(R.id.toolbargh);
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

        txtnull_list=findViewById(R.id.txtnull_list);
        txtnull_list.setVisibility(View.GONE);

        tvMuatiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartListActivity.this, MainActivity.class));
            }
        });

        cartAdapter  = new CartAdapter(this, listDatmua);
        recyclerView_dat_mua = findViewById(R.id.listDatmua);
        txtTongtien=findViewById(R.id.txtTongtien);
        btnnext = (Button) findViewById(R.id.next);

        sessionManager= new SessionManager(getApplicationContext());

        HashMap<String,String> user = sessionManager.getUserDetail();
        quyen = user.get(sessionManager.QUYEN);
        name = user.get(sessionManager.NAME);
        idUser = user.get(sessionManager.ID);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_dat_mua.setLayoutManager(gridLayoutManager);
        recyclerView_dat_mua.setHasFixedSize(true);

        fetchDatmua(idUser);
    }
    public void fetchDatmua(String key){
        apiInTerFaceDatmua = ApiClient.getApiClient().create(ApiInTerFaceDatmua.class);
        Call<List<DatMua>> call = apiInTerFaceDatmua.getDatMua(key);

        call.enqueue(new Callback<List<DatMua>>() {
            @Override
            public void onResponse(Call<List<DatMua>> call, retrofit2.Response<List<DatMua>> response) {
                listDatmua= response.body();
                sizeList = listDatmua.size();
                cartAdapter = new CartAdapter(CartListActivity.this,listDatmua);
                recyclerView_dat_mua.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();

                if (listDatmua.size() == 0){
                    tv_tongthanhtoan.setVisibility(View.GONE);
                    txtDonvi.setVisibility(View.GONE);
                    btnnext.setVisibility(View.GONE);
                    txtTongtien.setVisibility(View.GONE);
                    txtnull_list.setVisibility(View.VISIBLE);
                    recyclerView_dat_mua.setVisibility(View.GONE
                    );
                }else {
                    tv_tongthanhtoan.setVisibility(View.VISIBLE);
                    txtDonvi.setVisibility(View.VISIBLE);
                    btnnext.setVisibility(View.VISIBLE);
                    txtTongtien.setVisibility(View.VISIBLE);
                    txtnull_list.setVisibility(View.GONE);
                    recyclerView_dat_mua.setVisibility(View.VISIBLE);
                    total =0;
                    for (int m =0; m<listDatmua.size();m++){
                        if (listDatmua.get(m).getSelected() == 1){
                            DatMua datMua = listDatmua.get(m);
                            int id = datMua.getId();
                            int gia = datMua.getGia();
                            int soluongg= datMua.getSoluong();

                            int tongTienTungsach = gia*soluongg;

                            total+= tongTienTungsach;
                        }
                    }
                    txtTongtien.setText(total+" ");
                    btnnext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (total == 0){
                                Toast.makeText(CartListActivity.this, "Bạn chưa chọn sản phẩm nào!", Toast.LENGTH_SHORT).show();
                            }else  {
                                Intent intent = new Intent(getBaseContext(), CartDetailActivity.class);
                                sessionManager.createTongtien(txtTongtien.getText().toString().trim());
                                startActivity(intent);
                            }

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<DatMua>> call, Throwable t) {
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
