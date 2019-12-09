package iron2014.bansachonline.adapter.hoadoncthd;

import android.app.Dialog;
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

import iron2014.bansachonline.Activity.hoadon.Chitiethoadon_RatingActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.model.Hoadon;

public class HoadonRatingAdapter extends RecyclerView.Adapter<HoadonRatingAdapter.MyViewHolder> {

    Context context;
    List<Hoadon> mData;
    Dialog myDialog;
    SessionManager sessionManager;
    private ProductItemActionListener actionListener;

    public HoadonRatingAdapter(Context context, List<Hoadon> mData) {
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

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        holder.tv_hoadon_stt.setText(context.getString(R.string.hoadonAdapter)+" "+mData.get(i).getMahoadon());
        holder.tv_tongtien.setText(mData.get(i).getTongtien()+"₫");

        holder.txtXemchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chitiethoadon_RatingActivity.class);
                String mahd = String.valueOf(mData.get(i).getMahoadon());
                String tinhtrang = mData.get(0).getTinhtrang();
                intent.putExtra("mahd", mahd);
                sessionManager.createHoadon(tinhtrang);
                context.startActivity(intent);
            }
        });
        holder.txtUoctinh.setVisibility(View.GONE);
        holder.txtUserXacnhan.setVisibility(View.GONE);
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
        TextView txtUserXacnhan;
        LinearLayout linnear_hoadon;
        //tùy từng tab
        private TextView txtUoctinh;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_hoadon_stt=(TextView)itemView.findViewById(R.id.tv_hoadon_stt);
            tv_tongtien=(TextView)itemView.findViewById(R.id.tv_tongtien);
            txtXemchitiet=(TextView) itemView.findViewById(R.id.txtXemchitiet);

            txtUoctinh=(TextView) itemView.findViewById(R.id.txtUoctinhnhan);
            txtUserXacnhan= itemView.findViewById(R.id.txtUserXacnhan);
            linnear_hoadon=itemView.findViewById(R.id.linnear_hoadon);
        }
    }
    public interface ProductItemActionListener{
        void onItemTap(ImageView imageView);
    }
}
