package iron2014.bansachonline.Fragment.donhang;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceHoadon;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.hoadoncthd.HoadonAdapter;
import iron2014.bansachonline.model.Hoadon;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChoXacNhanFragment extends Fragment {
    private RecyclerView recyclerview_choxacnhan;
    private HoadonAdapter hoadonAdapter;
    private List<Hoadon> listHoadon = new ArrayList<>();
    ApiInTerFaceHoadon apiInTerFaceHoadon;
    private SessionManager sessionManager;
    private TextView txtBill_empty;
    String mauser;
    public ChoXacNhanFragment() {
        // Required empty public constructor
    }
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cho_xac_nhan, container, false);
        addControls();
        sessionManager = new SessionManager(getContext());
        StaggeredGridLayoutManager gridLayoutManagerVeticl =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_choxacnhan.setLayoutManager(gridLayoutManagerVeticl);
        recyclerview_choxacnhan.setHasFixedSize(true);

        HashMap<String,String> user = sessionManager.getUserDetail();
        mauser = user.get(sessionManager.ID);

        fetchHoadon(mauser);

        return v;
    }
    public void fetchHoadon(String miduser){
        apiInTerFaceHoadon = ApiClient.getApiClient().create(ApiInTerFaceHoadon.class);
        Call<List<Hoadon>> call = apiInTerFaceHoadon.get_choxacnhan(miduser);

        call.enqueue(new Callback<List<Hoadon>>() {
            @Override
            public void onResponse(Call<List<Hoadon>> call, retrofit2.Response<List<Hoadon>> response) {
                if (response.body().size() == 0){
                    txtBill_empty.setVisibility(View.VISIBLE);
                    recyclerview_choxacnhan.setVisibility(View.GONE);
                }else {
                    txtBill_empty.setVisibility(View.GONE);
                    recyclerview_choxacnhan.setVisibility(View.VISIBLE);
                    //progressBar.setVisibility(View.GONE);
                    listHoadon= response.body();
                    hoadonAdapter = new HoadonAdapter(getContext(),listHoadon);
                    recyclerview_choxacnhan.setAdapter(hoadonAdapter);
                    hoadonAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<Hoadon>> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    private void addControls(){
        recyclerview_choxacnhan = v.findViewById(R.id.recyclerview_choxacnhan);
        txtBill_empty = v.findViewById(R.id.txtBill_empty);
    }
}
