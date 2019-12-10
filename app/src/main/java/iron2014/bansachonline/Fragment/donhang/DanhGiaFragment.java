package iron2014.bansachonline.Fragment.donhang;


import android.content.Intent;
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

import iron2014.bansachonline.Activity.hoadon.ChitiethoadonActivity;
import iron2014.bansachonline.Activity.hoadon.Chitiethoadon_RatingActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceHoadon;
import iron2014.bansachonline.R;
import iron2014.bansachonline.RecycerViewTouch.RecyclerTouchListener;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.hoadoncthd.HoadonRatingAdapter;
import iron2014.bansachonline.model.Hoadon;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class DanhGiaFragment extends Fragment {
    private RecyclerView recyclerview_danhgia;
    private HoadonRatingAdapter hoadonAdapter;
    private List<Hoadon> listHoadon = new ArrayList<>();
    ApiInTerFaceHoadon apiInTerFaceHoadon;
    private SessionManager sessionManager;
    String mauser;
    TextView txtBill_empty_danhgia;
    public DanhGiaFragment() {
        // Required empty public constructor
    }
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_danh_gia, container, false);
        addControls();
        sessionManager = new SessionManager(getContext());
        StaggeredGridLayoutManager gridLayoutManagerVeticl =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_danhgia.setLayoutManager(gridLayoutManagerVeticl);
        recyclerview_danhgia.setHasFixedSize(true);

        HashMap<String,String> user = sessionManager.getUserDetail();
        mauser = user.get(sessionManager.ID);

        fetchHoadon(mauser);

        recyclerview_danhgia.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerview_danhgia, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Hoadon theloai =   listHoadon.get(position);
                String id = String.valueOf(theloai.getMahoadon());
                String ten = theloai.getTenkh();
                String diachi = theloai.getDiachi();
                String sdt = theloai.getSdt();
                String tinhtrang = theloai.getTinhtrang();
                String tongtien = String.valueOf(theloai.getTongtien());
                String mauser = String.valueOf(theloai.getMauser());
                String ngayxuat = theloai.getNgayxuat();


                sessionManager.createSessionGuimatheloai(id,ten);
                Intent intent = new Intent(getContext(), ChitiethoadonActivity.class);
                intent.putExtra("mahoadon", id);
                intent.putExtra("tenkh", ten);
                intent.putExtra("diachi", diachi);
                intent.putExtra("sdt", sdt);
                intent.putExtra("tinhtrang", tinhtrang);
                intent.putExtra("tongtien", tongtien);
                intent.putExtra("mauser", mauser);
                intent.putExtra("ngayxuat",ngayxuat);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {


            }
        }));
        return v;
    }
    public void fetchHoadon(String miduser){
        apiInTerFaceHoadon = ApiClient.getApiClient().create(ApiInTerFaceHoadon.class);
        Call<List<Hoadon>> call = apiInTerFaceHoadon.get_danhgia(miduser);

        call.enqueue(new Callback<List<Hoadon>>() {
            @Override
            public void onResponse(Call<List<Hoadon>> call, retrofit2.Response<List<Hoadon>> response) {
                if (response.body().size() == 0){
                    txtBill_empty_danhgia.setVisibility(View.VISIBLE);
                    recyclerview_danhgia.setVisibility(View.GONE);
                }else {
                    txtBill_empty_danhgia.setVisibility(View.GONE);
                    recyclerview_danhgia.setVisibility(View.VISIBLE);
                    //progressBar.setVisibility(View.GONE);
                    listHoadon= response.body();
                    hoadonAdapter = new HoadonRatingAdapter(getContext(),listHoadon);
                    recyclerview_danhgia.setAdapter(hoadonAdapter);
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
        recyclerview_danhgia = v.findViewById(R.id.recyclerview_danhgia);
        txtBill_empty_danhgia = v.findViewById(R.id.txtBill_empty_danhgia);
    }
}
