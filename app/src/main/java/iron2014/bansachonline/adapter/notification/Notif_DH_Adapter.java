package iron2014.bansachonline.adapter.notification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import iron2014.bansachonline.MuahangActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.model.Notification;

public class Notif_DH_Adapter extends RecyclerView.Adapter<Notif_DH_Adapter.MyViewHolder> {

    Context context;
    List<Notification> mData;
    String tieude;
    int mahoadon;
    public Notif_DH_Adapter(Context context, List<Notification> mData) {
        this.context = context;
        this.mData = mData;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_notif_donhang, viewGroup, false);

        final MyViewHolder viewHolder= new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        String thoigian = mData.get(i).getThoigian();
        holder.txtMota_tieude_dh.setText(mData.get(i).getTieude());
        holder.txtMota_notif_dh.setText(mData.get(i).getMota());
        holder.txtThoigian_dh.setText(thoigian);
//        String urlImage = mData.get(i).getHinhanh();
//        try {
//            Picasso.with(context).load(urlImage).into(myViewHolder.img);
//        }catch (Exception e){
//            Log.e("log img", e.toString());
//        }
        holder.img_dh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tieude = mData.get(i).getTieude();
                mahoadon = mData.get(i).getMahoadon();
                Intent intent = new Intent(context, MuahangActivity.class);
                if (tieude.equals("Chờ xử lý đơn hàng")){
                    intent.putExtra("check","0");
                }else if (tieude.equals("Đơn hàng  "+mahoadon+" đang được giao đến bạn.")){
                    intent.putExtra("check","1");
                }
                else if (tieude.equals("Đơn hàng  "+mahoadon+" giao thành công.")){
                    intent.putExtra("check","2");
                }
                context.startActivity(intent);
            }
        });
        holder.linnear_giua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tieude = mData.get(i).getTieude();
                mahoadon = mData.get(i).getMahoadon();
                Intent intent = new Intent(context, MuahangActivity.class);
                if (tieude.equals("Chờ xử lý đơn hàng")){
                    intent.putExtra("check","0");
                }else if (tieude.equals("Đơn hàng "+mahoadon+" đang được giao đến bạn.")){
                    intent.putExtra("check","1");
                }
                else if (tieude.equals("Đơn hàng "+mahoadon+" giao thành công.")){
                    intent.putExtra("check","2");
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_dh;
        private TextView txtMota_tieude_dh;
        private TextView txtMota_notif_dh;
        private TextView txtThoigian_dh;
        private TextView tvXemchitiet, tvAnchitiet;
        private LinearLayout lnMota,linnear_giua;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvXemchitiet = (TextView)itemView.findViewById(R.id.tvXemchitiet);
            tvAnchitiet = (TextView)itemView.findViewById(R.id.tvAnchitiet);
            txtMota_tieude_dh=(TextView)itemView.findViewById(R.id.txtMota_tieude_dh);
            txtMota_notif_dh=(TextView)itemView.findViewById(R.id.txtMota_notif_dh);
            txtThoigian_dh = (TextView)itemView.findViewById(R.id.txtThoigian_dh);
            img_dh=(ImageView) itemView.findViewById(R.id.img_dh);
            linnear_giua = itemView.findViewById(R.id.linnear_giua);

            tvAnchitiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txtMota_notif_dh.setVisibility(View.GONE);
                    txtThoigian_dh.setVisibility(View.GONE);
                    tvAnchitiet.setVisibility(View.GONE);
                    tvXemchitiet.setVisibility(View.VISIBLE);
                }
            });

            tvXemchitiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txtMota_notif_dh.setVisibility(View.VISIBLE);
                    txtThoigian_dh.setVisibility(View.VISIBLE);
                    tvXemchitiet.setVisibility(View.GONE);
                    tvAnchitiet.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
