package iron2014.bansachonline.Fragment.donhang;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iron2014.bansachonline.Activity.hoadon.ChitiethoadonActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceHoadon;
import iron2014.bansachonline.R;
import iron2014.bansachonline.RecycerViewTouch.RecyclerTouchListener;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.hoadoncthd.HoadonAdapter;
import iron2014.bansachonline.model.Hoadon;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class DangGiaoFragment extends Fragment {
    private RecyclerView recyclerview_danggiao,recyclerview_userxacnhan;
    private HoadonAdapter hoadonAdapter;
    private List<Hoadon> listHoadon = new ArrayList<>();

    private List<Hoadon> listHoadon_userxacnhan = new ArrayList<>();
    private ApiInTerFaceHoadon apiInTerFaceHoadon;
    private SessionManager sessionManager;
    private String mauser;
    private TextView txtBill_empty_danggiao;

    int sizeList_danggiao =0;
    int sizeList_userxacnhan =0;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_dang_giao, container, false);
        addControls();
        sessionManager = new SessionManager(getContext());
        StaggeredGridLayoutManager gridLayoutManagerVeticl =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_danggiao.setLayoutManager(gridLayoutManagerVeticl);
        recyclerview_danggiao.setHasFixedSize(true);

        StaggeredGridLayoutManager gridLayoutManagerVeticl2 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_userxacnhan.setLayoutManager(gridLayoutManagerVeticl2);
        recyclerview_userxacnhan.setHasFixedSize(true);

        HashMap<String,String> user = sessionManager.getUserDetail();
        mauser = user.get(sessionManager.ID);

        fetchHoadon(mauser);
        fetchHoadon_userxacnhan(mauser);


        recyclerview_danggiao.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerview_danggiao, new RecyclerTouchListener.ClickListener() {
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

                sessionManager.createSessionGuimatheloai(id,ten);
                Intent intent = new Intent(getContext(), ChitiethoadonActivity.class);
                intent.putExtra("mahoadon", id);
                intent.putExtra("tenkh", ten);
                intent.putExtra("diachi", diachi);
                intent.putExtra("sdt", sdt);
                intent.putExtra("tinhtrang", tinhtrang);
                intent.putExtra("tongtien", tongtien);
                intent.putExtra("mauser", mauser);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {


            }
        }));
        recyclerview_userxacnhan.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerview_userxacnhan, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Hoadon theloai =   listHoadon_userxacnhan.get(position);
                String id = String.valueOf(theloai.getMahoadon());
                String ten = theloai.getTenkh();
                String diachi = theloai.getDiachi();
                String sdt = theloai.getSdt();
                String tinhtrang = theloai.getTinhtrang();
                String tongtien = String.valueOf(theloai.getTongtien());
                String mauser = String.valueOf(theloai.getMauser());

                sessionManager.createSessionGuimatheloai(id,ten);
                Intent intent = new Intent(getContext(), ChitiethoadonActivity.class);
                intent.putExtra("mahoadon", id);
                intent.putExtra("tenkh", ten);
                intent.putExtra("diachi", diachi);
                intent.putExtra("sdt", sdt);
                intent.putExtra("tinhtrang", tinhtrang);
                intent.putExtra("tongtien", tongtien);
                intent.putExtra("mauser", mauser);
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
        Call<List<Hoadon>> call = apiInTerFaceHoadon.get_danggiao(miduser);

        call.enqueue(new Callback<List<Hoadon>>() {
            @Override
            public void onResponse(Call<List<Hoadon>> call, retrofit2.Response<List<Hoadon>> response) {
                    sizeList_danggiao = response.body().size();
                    //progressBar.setVisibility(View.GONE);
                    listHoadon= response.body();
                    hoadonAdapter = new HoadonAdapter(getContext(),listHoadon);
                    recyclerview_danggiao.setAdapter(hoadonAdapter);
                    hoadonAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Hoadon>> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }

    public void fetchHoadon_userxacnhan(String miduser){
        apiInTerFaceHoadon = ApiClient.getApiClient().create(ApiInTerFaceHoadon.class);
        Call<List<Hoadon>> call = apiInTerFaceHoadon.get_userxacnhan(miduser);

        call.enqueue(new Callback<List<Hoadon>>() {
            @Override
            public void onResponse(Call<List<Hoadon>> call, retrofit2.Response<List<Hoadon>> response) {
                    sizeList_userxacnhan = response.body().size();
                    checkNull();

                    recyclerview_userxacnhan.setVisibility(View.VISIBLE);
                    //progressBar.setVisibility(View.GONE);
                    listHoadon_userxacnhan= response.body();
                    hoadonAdapter = new HoadonAdapter(getContext(),listHoadon_userxacnhan);
                    recyclerview_userxacnhan.setAdapter(hoadonAdapter);
                    hoadonAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Hoadon>> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    private void  checkNull() {
        if (sizeList_danggiao == 0 && sizeList_userxacnhan ==0 ){
            txtBill_empty_danggiao.setVisibility(View.VISIBLE);
            recyclerview_danggiao.setVisibility(View.GONE);
            recyclerview_userxacnhan.setVisibility(View.GONE);
        }else {
            txtBill_empty_danggiao.setVisibility(View.GONE);
            recyclerview_danggiao.setVisibility(View.VISIBLE);
            recyclerview_userxacnhan.setVisibility(View.VISIBLE);
        }
    }
    private void addControls(){
        recyclerview_danggiao = v.findViewById(R.id.recyclerview_danggiao);
        txtBill_empty_danggiao=v.findViewById(R.id.txtBill_empty_danggiao);

        recyclerview_userxacnhan= v.findViewById(R.id.recyclerview_userxacnhan);
    }
}
