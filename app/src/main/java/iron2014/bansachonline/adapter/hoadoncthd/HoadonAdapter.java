package iron2014.bansachonline.adapter.hoadoncthd;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import iron2014.bansachonline.Activity.hoadon.ChitiethoadonActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.model.Hoadon;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;

public class HoadonAdapter extends RecyclerView.Adapter<HoadonAdapter.MyViewHolder> {

    Context context;
    List<Hoadon> mData;
    Dialog myDialog;
    SessionManager sessionManager;
    private ProductItemActionListener actionListener;
    // Định dạng thời gian
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Calendar defaultTime = Calendar.getInstance();
    SharedPref sharedPref;
    public HoadonAdapter(Context context, List<Hoadon> mData) {
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
        view = LayoutInflater.from(context).inflate(R.layout.item_hoadon, viewGroup, false);

        final MyViewHolder viewHolder= new MyViewHolder(view);
        sessionManager = new SessionManager(context);
        sharedPref = new SharedPref(context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        holder.tv_hoadon_stt.setText("Hóa đơn "+mData.get(i).getMahoadon());
        holder.tv_tongtien.setText(mData.get(i).getTongtien()+"₫");

        holder.txtXemchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChitiethoadonActivity.class);
                String mahd = String.valueOf(mData.get(i).getMahoadon());
                String tinhtrang = mData.get(i).getTinhtrang();
                intent.putExtra("mahd", mahd);
                intent.putExtra("tinhtrang", tinhtrang);
                sessionManager.createHoadon(tinhtrang);
                context.startActivity(intent);
            }
        });
        int amount = 3;
        Date date = java.sql.Date.valueOf(mData.get(i).getNgayxuat());
        defaultTime.setTime(date);
        defaultTime.roll(Calendar.DATE, amount);

        String limitTime =(dateFormat.format(defaultTime.getTime()));

        if (mData.get(i).getTinhtrang().equals("choxacnhan")){
            holder.txtUserXacnhan.setVisibility(View.GONE);
            holder.txtUoctinh.setText("Xác nhận trước "+limitTime);
        }else if (mData.get(i).getTinhtrang().equals("cholayhang")){
            holder.txtUserXacnhan.setVisibility(View.GONE);
            holder.txtUoctinh.setText("Chuyển đi trước "+limitTime);
        }else if (mData.get(i).getTinhtrang().equals("danggiao")){
            holder.txtUserXacnhan.setVisibility(View.GONE);
            holder.txtUoctinh.setText("Nhận hàng trước "+limitTime);
        }else  if (mData.get(i).getTinhtrang().equals("userxacnhan")){
            holder.txtUserXacnhan.setVisibility(View.VISIBLE);
            holder.txtUoctinh.setText("Nhận hàng trước "+limitTime);
        }else  if (mData.get(i).getTinhtrang().equals("danhgia")){
            holder.txtUserXacnhan.setVisibility(View.GONE);
            holder.txtUoctinh.setText("Nhận hàng trước "+limitTime);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout item_contact;
        private TextView tv_hoadon_stt;
        private TextView tv_tongtien;
        private TextView txtXemchitiet;
        //tùy từng tab
        private TextView txtUoctinh;
        private TextView txtUserXacnhan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_hoadon_stt=(TextView)itemView.findViewById(R.id.tv_hoadon_stt);
            tv_tongtien=(TextView)itemView.findViewById(R.id.tv_tongtien);
            txtXemchitiet=(TextView) itemView.findViewById(R.id.txtXemchitiet);

            txtUoctinh=(TextView) itemView.findViewById(R.id.txtUoctinhnhan);
            txtUserXacnhan= itemView.findViewById(R.id.txtUserXacnhan);
        }
    }
    public static void UpDownDate() {

        // Định dạng thời gian
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        // Định nghĩa mốc thời gian ban đầu là ngày 2019-11-15

        Date date = java.sql.Date.valueOf("2019-11-15");

        c1.setTime(date);
        c2.setTime(date);

        Log.d("Ngày ban đầu","Ngày ban đầu : " + dateFormat.format(c1.getTime()));

        // Tăng ngày thêm 8 ngày -- Sử dụng phương thức roll()
        c1.roll(Calendar.DATE, 8);

        // c1.roll(Calendar.DATE, -8); // Giảm ngày 8 ngày ==> 23-07-2011
        System.out.println("Ngày được tăng thêm 8 ngày (Sử dụng Roll) : "+ dateFormat.format(c1.getTime()));


        /* Các trường hợp khác
        c1.roll(Calendar.DATE, true); //Tăng 1 ngày -- Nếu muốn giảm một ngày truyền vào false
        c1.roll(Calendar.MONTH, 2);   //Tăng lên 2 tháng
        c1.roll(Calendar.YEAR, 2) ;      //Tăng lên 2 năm
        */

        // Tăng ngày thêm 8 ngày -- Sử dụng phương thức add()
        c2.add(Calendar.DATE, 8);
        //c2.add(Calendar.DATE, -8); // Giảm ngày 8 ngày ==> 23-07-2011
        System.out.println("Ngày được tăng thêm 8 ngày (Sử dụng add)  : "
                + dateFormat.format(c2.getTime()));


    }
    public interface ProductItemActionListener{
        void onItemTap(ImageView imageView);
    }
}
