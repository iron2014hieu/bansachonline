package iron2014.bansachonline.fragmentMain;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iron2014.bansachonline.Activity.CartListActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceDatmua;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceHoadon;
import iron2014.bansachonline.LoginRegister.LoginActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.KhuyenMai.KhuyenMaiAdapter;
import iron2014.bansachonline.model.DatMua;
import iron2014.bansachonline.model.KhuyenMai;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class TheloaiFragment extends Fragment {
    private RecyclerView recyclerview_khuyenmai;
    private KhuyenMaiAdapter khuyenMaiAdapter;
    private List<KhuyenMai> listHoadon = new ArrayList<>();
    ApiInTerFaceHoadon apiInTerFaceHoadon;
    private SessionManager sessionManager;
    View view;
    AdView adView;
    TextView counttxt_thuviern;
    ApiInTerFaceDatmua apiInTerFaceDatmua;
    ImageView chk_icon_km;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_theloai, container, false);

        addControls();

        sessionManager = new SessionManager(getContext());
        StaggeredGridLayoutManager gridLayoutManagerVeticl =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_khuyenmai.setLayoutManager(gridLayoutManagerVeticl);
        recyclerview_khuyenmai.setHasFixedSize(true);
        fetchHoadon();
        //ad Admob
        MobileAds.initialize(getContext(),"ca-app-pub-4271678036251490~7420544422");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        final HashMap<String,String> user = sessionManager.getUserDetail();
        fetchSoluong(user.get(sessionManager.ID));
        chk_icon_km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.get(sessionManager.ID)!=null) {
                    Intent intent = new Intent(getContext(), CartListActivity.class);
                    startActivity(intent);
                }else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });
        return  view;
    }
    public void fetchSoluong(String mauser){
        apiInTerFaceDatmua = ApiClient.getApiClient().create(ApiInTerFaceDatmua.class);
        Call<List<DatMua>> call = apiInTerFaceDatmua.get_soluong(mauser);

        call.enqueue(new Callback<List<DatMua>>() {
            @Override
            public void onResponse(Call<List<DatMua>> call, retrofit2.Response<List<DatMua>> response) {
                int total =0;
                int soluong =0;
                for (int i = 0; i<response.body().size(); i++){
                    soluong = response.body().get(i).getSoluong();
                    total+= soluong;

                }
                counttxt_thuviern.setText(String.valueOf(total));
            }

            @Override
            public void onFailure(Call<List<DatMua>> call, Throwable t) {
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    public void fetchHoadon(){
        apiInTerFaceHoadon = ApiClient.getApiClient().create(ApiInTerFaceHoadon.class);
        Call<List<KhuyenMai>> call = apiInTerFaceHoadon.get_all_khuyenmai();
        call.enqueue(new Callback<List<KhuyenMai>>() {
            @Override
            public void onResponse(Call<List<KhuyenMai>> call, retrofit2.Response<List<KhuyenMai>> response) {
                //progressBar.setVisibility(View.GONE);
                if (response.body().size() == 0){
                    recyclerview_khuyenmai.setVisibility(View.GONE);
                }else {
                    recyclerview_khuyenmai.setVisibility(View.VISIBLE);
                    listHoadon= response.body();
                    khuyenMaiAdapter = new KhuyenMaiAdapter(getContext(),listHoadon);
                    recyclerview_khuyenmai.setAdapter(khuyenMaiAdapter);
                    khuyenMaiAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<KhuyenMai>> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    private void addControls(){
        recyclerview_khuyenmai = view.findViewById(R.id.recyclerview_khuyenmai);
        adView = view.findViewById(R.id.adView);
        counttxt_thuviern= view.findViewById(R.id.counttxt_thuviern);
        chk_icon_km= view.findViewById(R.id.chk_icon_km);
    }
}
