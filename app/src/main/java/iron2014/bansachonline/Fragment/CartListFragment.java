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
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.CartAdapter;
import iron2014.bansachonline.model.DatMua;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartListFragment extends Fragment {

    TextView tvMuatiep, tvTTgiohang;

    View view;
    ApiInTerFaceDatmua apiInTerFaceDatmua;
    CartAdapter cartAdapter;
    RecyclerView recyclerView_dat_mua;
    private List<DatMua> listDatmua = new ArrayList<>();
    public  static CheckBox checkbox_cartlist;
    int sizeList;
    public  static TextView txtTongtien;
    private Button btnnext;
    String quyen, name, idUser;
    SessionManager sessionManager;
    public static int total=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart_list, container, false);
        tvMuatiep = view.findViewById(R.id.tvMuatiep);
        tvMuatiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), GetAllBookActivity.class);
                startActivity(i);
            }
        });

        cartAdapter  = new CartAdapter(getContext(), listDatmua);
        recyclerView_dat_mua = view.findViewById(R.id.listDatmua);
        txtTongtien=view.findViewById(R.id.txtTongtien);
        tvTTgiohang = view.findViewById(R.id.tvTTgiohang);
        btnnext = (Button) view.findViewById(R.id.next);
        checkbox_cartlist=(CheckBox) view.findViewById(R.id.checkbox_cartlist);

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

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            Intent intent = new Intent(getContext(), CartDetailActivity.class);
                            intent.putExtra("tongtien", String.valueOf(total));
                            startActivity(intent);
            }
        });
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
                    btnnext.setVisibility(View.GONE);
                    txtTongtien.setVisibility(View.GONE);
                    tvTTgiohang.setVisibility(View.VISIBLE);
                }else {
                    btnnext.setVisibility(View.VISIBLE);
                    txtTongtien.setVisibility(View.VISIBLE);
                    tvTTgiohang.setVisibility(View.GONE);
                    for (int i=0;i<sizeList;i++){
                        DatMua datMua = listDatmua.get(i);
                        total = (Integer.valueOf(datMua.getGia()))*(Integer.valueOf(datMua.getSoluong()));

                        txtTongtien.setText(total+" ");
                    }

                }
            }

            @Override
            public void onFailure(Call<List<DatMua>> call, Throwable t) {
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    private List<DatMua> getModel(int isSelect){
        for(int i = 0; i < sizeList ; i++){

            DatMua datMua = new DatMua();
            listDatmua.get(i).setSelected(1);
//            listDatmua.add(datMua);
        }
        return listDatmua;
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
    private void loadListCart() {
        cartAdapter.notifyDataSetChanged();
    }
}
