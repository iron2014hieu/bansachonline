package iron2014.bansachonline.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import iron2014.bansachonline.Activity.GetAllBookActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceDatmua;
import iron2014.bansachonline.CartDetailActivity;
import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.CartAdapter;
import iron2014.bansachonline.fragmentMain.HomeFragment;
import iron2014.bansachonline.fragmentMain.TheloaiFragment;
import iron2014.bansachonline.model.DatMua;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartListFragment extends Fragment {

    TextView tvMuatiep, tv_tongthanhtoan, txtDonvi, tvLaymaKM,txtnull_list;

    View view;
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
    public static int total1=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart_list, container, false);
        tv_tongthanhtoan = view.findViewById(R.id.tv_tongthanhtoan);
        txtDonvi = view.findViewById(R.id.txtDonvi);
        tvMuatiep = view.findViewById(R.id.tvMuatiep);

        txtnull_list= view.findViewById(R.id.txtnull_list);
        txtnull_list.setVisibility(View.GONE);

        tvMuatiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        cartAdapter  = new CartAdapter(getContext(), listDatmua);
        recyclerView_dat_mua = view.findViewById(R.id.listDatmua);
        txtTongtien=view.findViewById(R.id.txtTongtien);
        btnnext = (Button) view.findViewById(R.id.next);

        sessionManager= new SessionManager(getContext());

        HashMap<String,String> user = sessionManager.getUserDetail();
        quyen = user.get(sessionManager.QUYEN);
        name = user.get(sessionManager.NAME);
        idUser = user.get(sessionManager.ID);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_dat_mua.setLayoutManager(gridLayoutManager);
        recyclerView_dat_mua.setHasFixedSize(true);
        fetchDatmua(idUser);


        txtTongtien.setText(" "+ CartAdapter.tongTienSach);
        return view;
    }


    public void fetchDatmua(String key){
        apiInTerFaceDatmua = ApiClient.getApiClient().create(ApiInTerFaceDatmua.class);
        Call<List<DatMua>> call = apiInTerFaceDatmua.getDatMua(key);

        call.enqueue(new Callback<List<DatMua>>() {
            @Override
            public void onResponse(Call<List<DatMua>> call, retrofit2.Response<List<DatMua>> response) {
                listDatmua= response.body();
                sizeList = listDatmua.size();
                cartAdapter = new CartAdapter(getContext(),listDatmua);
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
                            Intent intent = new Intent(getContext(), CartDetailActivity.class);
//                            intent.putExtra("tongtien", txtTongtien.getText().toString().trim());
                            sessionManager.createTongtien(txtTongtien.getText().toString().trim());
                            startActivity(intent);
                        }
                    });
                    total = 0;
                }
            }

            @Override
            public void onFailure(Call<List<DatMua>> call, Throwable t) {
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    public void update_selected( final String masach,final String selected, String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("tb")){
                        }else if (response.trim().equals("tc")){
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
                params.put("selected", selected);
                params.put("masach", masach);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
