package iron2014.bansachonline.adapter.Sach;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iron2014.bansachonline.Activity.BookDetailActivity;
import iron2014.bansachonline.Activity.CartListActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFace;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceDatmua;
import iron2014.bansachonline.CustomToast;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.URL.UrlSql;
import iron2014.bansachonline.model.Books;
import iron2014.bansachonline.model.DatMua;
import retrofit2.Call;
import retrofit2.Callback;

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.MyViewHolder> {

    int soluong =0;
    Context context;
    List<Books> mData;
    Dialog myDialog;
    SessionManager sessionManager;
    String iduser;
    ApiInTerFaceDatmua apiInTerFaceDatmua;
    ApiInTerFace apiInTerFace;
    private ProductItemActionListener actionListener;

    public SachAdapter(Context context, List<Books> mData) {
        this.context = context;
        this.mData = mData;
    }
    public void setActionListener(ProductItemActionListener actionListener) {
        this.actionListener = actionListener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_sach, viewGroup, false);

        final MyViewHolder viewHolder= new MyViewHolder(view);

        sessionManager = new SessionManager(context);
        HashMap<String,String> user = sessionManager.getUserDetail();
        iduser = user.get(sessionManager.ID);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv_name.setText(mData.get(i).getTensach());
        myViewHolder.tv_phone.setText("Giá: "+mData.get(i).getGia()+"₫");

//        myViewHolder.tv_soluongsach.setText(String.valueOf(mData.get(i).getSoluong()));
        if(mData.get(i).getSoluong()== 0) {
            myViewHolder.imghethang.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imghethang.setVisibility(View.GONE);
        }
        myViewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionListener!=null)
                    actionListener.onItemTap(myViewHolder.img);
            }
        });
        try {
            String urlImage = mData.get(i).getAnhbia();
            Picasso.with(context).load(urlImage).into(myViewHolder.img);
        }catch (Exception e){
            Log.e("IMG", e.toString());
        }
        Books books =   mData.get(i);
        final String masach = String.valueOf(books.getMasach());
        final String tensach = String.valueOf(books.getTensach());
        final String manxb = String.valueOf(books.getManxb());
        final String matheloai = String.valueOf(books.getMatheloai());
        final String ngayxb = books.getNgayxb();
        final String noidung = books.getNoidung();
        final String anhbia =books.getAnhbia();
        final String gia = String.valueOf( books.getGia());
        final String tennxb= String.valueOf(books.getTennxb());
        final String soluong = String.valueOf(books.getSoluong());
        final String tacgia = books.getTacgia();
        final String matacgia = String.valueOf(books.getMatacgia());


        final String tongdiem = String.valueOf(books.getTongdiem());
        final String landanhgia = String.valueOf(books.getLandanhgia());

        myViewHolder.tv_soluongsach.setText(soluong+ " cuốn");
        myViewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.createSessionSendInfomationBook(masach,tensach,manxb,matheloai,ngayxb,noidung,
                        anhbia,gia,tennxb,soluong,tacgia,matacgia, tongdiem, landanhgia);
                context.startActivity(new Intent(context, BookDetailActivity.class));
            }
        });
        if(iduser == null){
            myViewHolder.img_add_tocart.setVisibility(View.GONE);
        }
        myViewHolder.img_add_tocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchBookDetails(String.valueOf(mData.get(i).getMasach()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout item_contact;
        private TextView tv_name;
        private TextView tv_phone;
        private TextView tv_soluongsach;
        private TextView tv_sldaban;
        private RatingBar ratingBar;
        ImageView img, img_add_tocart;
        ImageView imghethang;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_soluongsach = itemView.findViewById(R.id.tv_soluongsach);
            tv_name=(TextView)itemView.findViewById(R.id.books_name);
            tv_phone=(TextView)itemView.findViewById(R.id.books_chitiet);
            img=(ImageView) itemView.findViewById(R.id.img_book_iv);
            tv_sldaban=(TextView) itemView.findViewById(R.id.tv_soluongsach);
            img_add_tocart= itemView.findViewById(R.id.img_add_tocart);
            imghethang =(ImageView) itemView.findViewById(R.id.imghethang);

        }
    }
    public interface ProductItemActionListener{
        void onItemTap(ImageView imageView);
    }
    public void fetchBookDetails(String masach){
        apiInTerFace = ApiClient.getApiClient().create(ApiInTerFace.class);
        Call<List<Books>> call = apiInTerFace.getBookDetail(masach);

        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, retrofit2.Response<List<Books>> response) {
                for (int m =0; m<response.body().size();m++){
                    Books books =response.body().get(m);
                    final int soluong = (books.getSoluong());
                    final String tensach = books.getTensach();
                    final String anhbia = books.getAnhbia();
                    final String masach =String.valueOf(books.getMasach());
                    final String gia = String.valueOf(books.getGia());
                    if (soluong>0){
                        ThemDatmua(masach, tensach, anhbia,iduser, gia,soluong);

                    }else {
                        Toast.makeText(context, "Sách "+tensach+" đã hết!", Toast.LENGTH_SHORT).show();
                    }
                }

            }



            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    private void ThemDatmua(final String masach, final String sp, final String hinhanhsach, final String mauser, final String giaban,final int sl_sach){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_INSERT_GIOHANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String check = jsonObject.getString("check");

                            if(check.equals("chuatontai")){

                                if (success.equals("1")){
                                    CustomToast.makeText(context,"Đã thêm vào giỏ hàng", (int) CustomToast.SHORT,CustomToast.SUCCESS,true).show();
                                }
                            }else {
//                                if (success.equals("1")){
//                                    Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
//                                }
                                Get_soluongDatmua(masach,sl_sach);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("masach", masach);
                params.put("sanpham", sp);
                params.put("hinhanh", hinhanhsach);
                params.put("gia", giaban);
                params.put("soluong", "1");
                params.put("mauser", mauser);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void Get_soluongDatmua(final String masach,final int sl_sach) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_GET_SOLUONG_GH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int sl_datmua = Integer.valueOf(response);
                        if (sl_datmua<sl_sach){
                            UpdateSoluongDatmua(masach);
                        }else {
                            CustomToast.makeText(context,"Sách không đủ số lượng", (int) CustomToast.SHORT,CustomToast.WARNING,true).show();
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
                params.put("masach", masach);
                params.put("mauser", iduser);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void UpdateSoluongDatmua(final String masach){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_UPDATE_SOLUONG_GH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("tc")){
                            CustomToast.makeText(context,"Đã thêm vào giỏ hàng!", (int) CustomToast.LONG,CustomToast.SUCCESS,true).show();
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
                params.put("masach", masach);
                params.put("mauser", iduser);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
