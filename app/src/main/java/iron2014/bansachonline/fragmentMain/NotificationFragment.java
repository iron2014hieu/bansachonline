package iron2014.bansachonline.fragmentMain;


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

import iron2014.bansachonline.Activity.GetBookByTheloaiActivity;
import iron2014.bansachonline.Activity.hoadon.ChitiethoadonActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFace;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceDatmua;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceNotif;
import iron2014.bansachonline.Main2Activity;
import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.MuahangActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.RecycerViewTouch.RecyclerTouchListener;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.Sach.SachAdapter;
import iron2014.bansachonline.adapter.notification.Notif_DH_Adapter;
import iron2014.bansachonline.adapter.notification.Notif_KM_Adapter;
import iron2014.bansachonline.model.Books;
import iron2014.bansachonline.model.DatMua;
import iron2014.bansachonline.model.Notification;
import iron2014.bansachonline.model.TheLoai;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    RecyclerView recyclerview_thongbao_donhang,recyclerview_thongbao_khuyenmai;
    TextView txtThongbaoNotif_null,txtCapnhatDonhang;
    View view;

    SessionManager sessionManager;
    private String mauser;
    ApiInTerFaceNotif apiInTerFaceNotif;

    List<Notification> listKM = new ArrayList<>();
    List<Notification>  listDH = new ArrayList<>();

    Notif_KM_Adapter notif_km_adapter;
    Notif_DH_Adapter notif_dh_adapter;
    TextView counttxt_notif;
    ApiInTerFaceDatmua apiInTerFaceDatmua;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        addcontrols();
        sessionManager = new SessionManager(getContext());
        notif_dh_adapter = new Notif_DH_Adapter(getContext(), listDH);
        notif_km_adapter = new Notif_KM_Adapter(getContext(), listKM);

        HashMap<String,String> user = sessionManager.getUserDetail();
        mauser = user.get(sessionManager.ID);
        if (mauser==null){
            txtThongbaoNotif_null.setVisibility(View.VISIBLE);
            recyclerview_thongbao_donhang.setVisibility(View.GONE);
            recyclerview_thongbao_khuyenmai.setVisibility(View.GONE);
            txtCapnhatDonhang.setVisibility(View.GONE);
        }
        StaggeredGridLayoutManager gridLayoutManagerVeticl =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_thongbao_khuyenmai.setLayoutManager(gridLayoutManagerVeticl);
        recyclerview_thongbao_khuyenmai.setHasFixedSize(true);

        StaggeredGridLayoutManager gridLayoutManagerVeticl1 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_thongbao_donhang.setLayoutManager(gridLayoutManagerVeticl1);
        recyclerview_thongbao_donhang.setHasFixedSize(true);
        fetchKhuyenmai();
        fetchDonhang(mauser);


        recyclerview_thongbao_khuyenmai.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerview_thongbao_khuyenmai, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("check", "1");
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {}})
        );
        fetchSoluong(mauser);

        return  view;
    }
    public void GotoCart(View view){
        startActivity(new Intent(getContext(), Main2Activity.class));
    }
    public void fetchSoluong(String mauser){
        apiInTerFaceDatmua = ApiClient.getApiClient().create(ApiInTerFaceDatmua.class);
        Call<List<DatMua>> call = apiInTerFaceDatmua.get_soluong(mauser);

        call.enqueue(new Callback<List<DatMua>>() {
            @Override
            public void onResponse(Call<List<DatMua>> call, retrofit2.Response<List<DatMua>> response) {
                int soluong =0;
                for (int i = 0; i<response.body().size(); i++){
                    soluong = response.body().get(i).getSoluong();
                }
                counttxt_notif.setText(String.valueOf(soluong));
            }

            @Override
            public void onFailure(Call<List<DatMua>> call, Throwable t) {
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    public void fetchKhuyenmai(){
        apiInTerFaceNotif = ApiClient.getApiClient().create(ApiInTerFaceNotif.class);
        Call<List<Notification>> call = apiInTerFaceNotif.get_notif_km();

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, retrofit2.Response<List<Notification>> response) {
                //progressBar.setVisibility(View.GONE);
                listKM= response.body();
                notif_km_adapter = new Notif_KM_Adapter(getContext(),listKM);
                recyclerview_thongbao_khuyenmai.setAdapter(notif_km_adapter);
                notif_km_adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    public void fetchDonhang(String mauser){
        apiInTerFaceNotif = ApiClient.getApiClient().create(ApiInTerFaceNotif.class);
        Call<List<Notification>> call = apiInTerFaceNotif.get_notif_dh(mauser);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, retrofit2.Response<List<Notification>> response) {
                //progressBar.setVisibility(View.GONE);
                listDH= response.body();
                notif_dh_adapter = new Notif_DH_Adapter(getContext(),listDH);
                recyclerview_thongbao_donhang.setAdapter(notif_dh_adapter);
                notif_dh_adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    private void addcontrols() {
        recyclerview_thongbao_donhang = view.findViewById(R.id.recyclerview_thongbao_donhang);
        recyclerview_thongbao_khuyenmai = view.findViewById(R.id.recyclerview_thongbao_khuyenmai);
        txtThongbaoNotif_null= view.findViewById(R.id.txtThongbaoNotif_null);
        txtCapnhatDonhang=view.findViewById(R.id.txtCapnhatDonhang);
        counttxt_notif= view.findViewById(R.id.counttxt_notif);
    }

}
