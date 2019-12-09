package iron2014.bansachonline.adapter.hoadoncthd;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import iron2014.bansachonline.Activity.hoadon.RatingBookCommentActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.model.CTHD;

public class CTHD_RatingAdapter extends RecyclerView.Adapter<CTHD_RatingAdapter.MyViewHolder> {

    Context context;
    List<CTHD> mData;
    Dialog myDialog;
    SessionManager sessionManager;
    private ProductItemActionListener actionListener;
    String tinhTrang;

    public CTHD_RatingAdapter(Context context, List<CTHD> mData) {
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
        view = LayoutInflater.from(context).inflate(R.layout.item_cthd_rating, viewGroup, false);
        final MyViewHolder viewHolder= new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        holder.txt_books_name_cthd.setText(context.getString(R.string.sach)+" "+mData.get(i).getTensach());
        holder.txt_giaban_cthd.setText(mData.get(i).getGiaban()+"₫");
        holder.txt_soluong_cthd.setText("x"+mData.get(i).getSoluong());
        try {
            Picasso.with(context).load(mData.get(i).getHinhanh()).into(holder.img_cthd);
        }catch (Exception e){}
        String diemdg = String.valueOf(mData.get(i).getDiemdanhgia());

        if (mData.get(i).getDiemdanhgia() == 0.0){
            holder.ratingbar_item_cthd.setVisibility(View.GONE);
            holder.txtDanhgia_sach.setVisibility(View.VISIBLE);
        }else {
            holder.ratingbar_item_cthd.setVisibility(View.VISIBLE);
            holder.txtDanhgia_sach.setVisibility(View.GONE);
            holder.ratingbar_item_cthd.setRating(Float.valueOf(diemdg));
        }

        holder.txtDanhgia_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RatingBookCommentActivity.class);
                String masach = String.valueOf(mData.get(i).getMasach());
                String idcthd = String.valueOf(mData.get(i).getId());
                intent.putExtra("masach", masach);
                intent.putExtra("idcthd", idcthd);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_soluong_cthd, tvTenkhachhang;
        private TextView txt_books_name_cthd;
        private TextView txt_giaban_cthd;
        private TextView txtDanhgia_sach;
        private RatingBar ratingbar_item_cthd;

        private ImageView img_cthd;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenkhachhang = (TextView)itemView.findViewById(R.id.tvTenkhachhang);
            txt_soluong_cthd=(TextView)itemView.findViewById(R.id.txt_soluong_cthd);
            txt_books_name_cthd=(TextView)itemView.findViewById(R.id.txt_books_name_cthd);
            txt_giaban_cthd=(TextView) itemView.findViewById(R.id.txt_giaban_cthd);
            txtDanhgia_sach=(TextView) itemView.findViewById(R.id.txtDanhgia_sach);
            ratingbar_item_cthd =itemView.findViewById(R.id.ratingbar_item_cthd);
            img_cthd = itemView.findViewById(R.id.img_cthd);
        }
    }
    public interface ProductItemActionListener{
        void onItemTap(ImageView imageView);
    }
}
