package iron2014.bansachonline.adapter.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import iron2014.bansachonline.R;
import iron2014.bansachonline.model.Notification;

public class Notif_DH_Adapter extends RecyclerView.Adapter<Notif_DH_Adapter.MyViewHolder> {

    Context context;
    List<Notification> mData;

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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMota_tieude_dh=(TextView)itemView.findViewById(R.id.txtMota_tieude_dh);
            txtMota_notif_dh=(TextView)itemView.findViewById(R.id.txtMota_notif_dh);
            txtThoigian_dh = (TextView)itemView.findViewById(R.id.txtThoigian_dh);
            img_dh=(ImageView) itemView.findViewById(R.id.img_dh);
        }
    }
}
