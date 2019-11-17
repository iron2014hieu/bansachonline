package iron2014.bansachonline.fragmentVanChuyen;


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
import java.util.List;

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
public class NhanHangFragment extends Fragment {
    private RecyclerView recyclerview_nhanhang;
    private HoadonAdapter hoadonAdapter;
    private List<Hoadon> listHoadon = new ArrayList<>();
    ApiInTerFaceHoadon apiInTerFaceHoadon;
    private SessionManager sessionManager;
    TextView txtBillEmpty;

    public NhanHangFragment() {
        // Required empty public constructor
    }

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_nhan_hang, container, false);
        addControls();
        sessionManager = new SessionManager(getContext());
        StaggeredGridLayoutManager gridLayoutManagerVeticl =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_nhanhang.setLayoutManager(gridLayoutManagerVeticl);
        recyclerview_nhanhang.setHasFixedSize(true);
        fetchHoadon();

        recyclerview_nhanhang.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerview_nhanhang, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Hoadon theloai =   listHoadon.get(position);
//                String id = String.valueOf(theloai.getMahoadon());
//                String ten = theloai.getTenkh();
//                String diachi = theloai.getDiachi();
//                String sdt = theloai.getSdt();
//                String tinhtrang = theloai.getTinhtrang();
//                String tongtien = String.valueOf(theloai.getTongtien());
//
//                sessionManager.createSessionGuimatheloai(id,ten);
//                Intent intent = new Intent(getContext(), ChitietVanChuyenActivity.class);
//                intent.putExtra("mahoadon", id);
//                intent.putExtra("tenkh", ten);
//                intent.putExtra("diachi", diachi);
//                intent.putExtra("sdt", sdt);
//                intent.putExtra("tinhtrang", tinhtrang);
//                intent.putExtra("tongtien", tongtien);
//                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {


            }
        }));
        return  v;
    }
    public void fetchHoadon(){
        apiInTerFaceHoadon = ApiClient.getApiClient().create(ApiInTerFaceHoadon.class);
        Call<List<Hoadon>> call = apiInTerFaceHoadon.get_all_danhgia();

        call.enqueue(new Callback<List<Hoadon>>() {
            @Override
            public void onResponse(Call<List<Hoadon>> call, retrofit2.Response<List<Hoadon>> response) {
                //progressBar.setVisibility(View.GONE);
                if (response.body().size() == 0){
                    txtBillEmpty.setVisibility(View.VISIBLE);
                    recyclerview_nhanhang.setVisibility(View.GONE);
                }else {
                    txtBillEmpty.setVisibility(View.GONE);
                    recyclerview_nhanhang.setVisibility(View.VISIBLE);
                    listHoadon= response.body();
                    hoadonAdapter = new HoadonAdapter(getContext(),listHoadon);
                    recyclerview_nhanhang.setAdapter(hoadonAdapter);
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
        recyclerview_nhanhang = v.findViewById(R.id.recyclerview_nhanhang);
        txtBillEmpty= v.findViewById(R.id.txtBill_empty_nhanhang);
    }
}