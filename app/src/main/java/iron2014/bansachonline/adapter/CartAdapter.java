package iron2014.bansachonline.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iron2014.bansachonline.Fragment.CartListFragment;
import iron2014.bansachonline.Main2Activity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.URL.UrlSql;
import iron2014.bansachonline.model.Cart;
import iron2014.bansachonline.model.DatMua;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    public static List<DatMua> listGiohang;
    public static int tongTienSach;
    private int tongTienTungsach;
    UrlSql urlSql;
    private  int total = 0;
    String iduser, masach;
    public  static String url_UD = "https://bansachonline.xyz/bansach/giohang/update_status_carts.php";

    public CartAdapter(Context context, List<DatMua> listGiohang) {
        this.context = context;
        this.listGiohang = listGiohang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_giohang, viewGroup, false);
        urlSql = new UrlSql();
        final MyViewHolder holder= new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        holder.checkBox.setTag(i);
        holder.tv_name.setText(listGiohang.get(i).getSanpham());
        holder.tv_giaban.setText(listGiohang.get(i).getGia()+" VNĐ");
        holder.btn_quality.setNumber(String.valueOf(listGiohang.get(i).getSoluong()));
        try {
            Picasso.with(context).load(listGiohang.get(i).getHinhanh()).into(holder.img_cart);
        }catch (Exception e){

        }
        final String maSach = String.valueOf(listGiohang.get(i).getMasach());

        if (listGiohang.get(i).getSelected()==1){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    update_selected(maSach,"1", url_UD);
                } else {
                    update_selected(maSach,"0", url_UD);
                }
            }
        });


        iduser = listGiohang.get(i).getMauser();
        masach = String.valueOf(listGiohang.get(i).getMasach());

        holder.btn_quality.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                DatMua datMua = listGiohang.get(i);
                String masach1 = String.valueOf(datMua.getMasach());
                updateSoluongTongtien(String.valueOf(newValue),iduser, masach1, UrlSql.URL_UPDATE_CARTS);
                datMua.setSoluong(newValue);
                total = (Integer.valueOf(datMua.getGia()))*(Integer.valueOf(datMua.getSoluong()));

                if (listGiohang.get(i).getSoluong()==0){
                    holder.checkBox.setChecked(false);
                    update_selected(maSach,"0", url_UD);
                }

            }
        });

        holder.btn_iv_Xoa_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String masach2 = String.valueOf(listGiohang.get(i).getMasach());
                AlertDialog.Builder alertDialog = new  AlertDialog.Builder(context);
                alertDialog.setMessage("Bạn có muốn xóa sách "+listGiohang.get(i).getSanpham()+" này không");
                alertDialog.setIcon(R.drawable.shockedelete);
                alertDialog.setTitle("Xóa sản phẩm");
                alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteGiohang(iduser, masach2,UrlSql.URL_DELETE_CARTS);
                    }
                });
                alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }

                });
                alertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return listGiohang.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private TextView tv_giaban;
        private CheckBox checkBox;
        private LinearLayout  linear_cart;
        private ImageView img_cart;
        private ImageView btn_iv_Xoa_giohang;
        private ElegantNumberButton btn_quality;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=(TextView)itemView.findViewById(R.id.tv_name_book_cart);
            tv_giaban=(TextView)itemView.findViewById(R.id.tv_giaban);
            checkBox=(CheckBox)itemView.findViewById(R.id.cb_giohang);
            linear_cart=(LinearLayout)itemView.findViewById(R.id.linear_cart);
            img_cart=(ImageView)itemView.findViewById(R.id.img_cart);

            btn_iv_Xoa_giohang= itemView.findViewById(R.id.btn_iv_Xoa_giohang);
            btn_quality = itemView.findViewById(R.id.btn_numberQuality);
        }
    }
    private void updateSoluongTongtien(final String soluong, final String mauser, final String masach, String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                params.put("soluong", soluong);
                params.put("mauser", mauser);
                params.put("masach", masach);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void deleteGiohang( final String mauser, final String masach, String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("tb")){
                        }else if (response.trim().equals("tc")){
                            context.startActivity(new Intent(context, Main2Activity.class));
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
                params.put("mauser", mauser);
                params.put("masach", masach);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void update_selected( final String masach,final String selected, String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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