package iron2014.bansachonline.adapter.notification;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import iron2014.bansachonline.R;
import iron2014.bansachonline.model.Notification;
import iron2014.bansachonline.model.Tacgia;

public class Notif_KM_Adapter extends RecyclerView.Adapter<Notif_KM_Adapter.MyViewHolder> {

    Context context;
    List<Notification> mData;

    public Notif_KM_Adapter(Context context, List<Notification> mData) {
        this.context = context;
        this.mData = mData;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_notif_khuyenmai, viewGroup, false);

        final MyViewHolder viewHolder= new MyViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        holder.txtMota_tieude_km.setText(mData.get(i).getTieude());
        holder.txtMota_notif_km.setText(mData.get(i).getMota());
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
        private ImageView img_km;
        private TextView txtMota_tieude_km;
        private TextView txtMota_notif_km;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMota_tieude_km=(TextView)itemView.findViewById(R.id.txtMota_tieude_km);
            txtMota_notif_km=(TextView)itemView.findViewById(R.id.txtMota_notif_km);
            img_km=(ImageView) itemView.findViewById(R.id.img_km);
        }
    }
}
