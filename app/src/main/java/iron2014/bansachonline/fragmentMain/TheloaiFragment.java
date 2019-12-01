package iron2014.bansachonline.fragmentMain;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceHoadon;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.KhuyenMai.KhuyenMaiAdapter;
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
    public TheloaiFragment() {
        // Required empty public constructor
    }
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
        return  view;
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
    }
}
