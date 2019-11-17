package iron2014.bansachonline.adapter.Sach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import iron2014.bansachonline.R;
import iron2014.bansachonline.model.Books;

public class SachTop123Adapter extends RecyclerView.Adapter<SachTop123Adapter.MyViewHolder> {

    Context context;
    List<Books> mData;
    private Boolean isFavorite=true;

    public SachTop123Adapter(Context context, List<Books> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_sach_top123, viewGroup, false);

        final MyViewHolder viewHolder= new MyViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv_name.setText(mData.get(i).getTensach());
        myViewHolder.tv_phone.setText(mData.get(i).getGia());
//        Double tongDiem = (mData.get(i).getTongdiem());
//        String lanDanhgia = String.valueOf(mData.get(i).getLandanhgia());
//
//        String diemanhgia =  String.valueOf((tongDiem)/Double.valueOf(lanDanhgia));
//        myViewHolder.ratingBar.setRating(Float.valueOf(diemanhgia));
//        myViewHolder.txtDanhgia.setText(diemanhgia+" điểm(với "+lanDanhgia+" đánh giá)");
        int stt = i+1;
        if (stt==1){
            myViewHolder.img_rank.setImageResource(R.drawable.gold);
        }else if (stt==2){
            myViewHolder.img_rank.setImageResource(R.drawable.silver);
        }else if (stt==3){
            myViewHolder.img_rank.setImageResource(R.drawable.bronze);
        }
//        myViewHolder.txtSTT.setText("Top "+String.valueOf(stt));
//        myViewHolder.img_favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Them yêu thich "+mData.get(i).getTensach(), Toast.LENGTH_SHORT).show();
//            }
//        });
        if (isFavorite==true){
            myViewHolder.img_un_favorite.setVisibility(View.GONE);
            myViewHolder.img_favorite.setVisibility(View.VISIBLE);
        }
        myViewHolder.img_un_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Thêm yêu thich "+mData.get(i).getTensach(), Toast.LENGTH_SHORT).show();
                myViewHolder.img_un_favorite.setVisibility(View.GONE);
                myViewHolder.img_favorite.setVisibility(View.VISIBLE);
            }
        });
        myViewHolder.img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Xóa yêu thich "+mData.get(i).getTensach(), Toast.LENGTH_SHORT).show();
                myViewHolder.img_un_favorite.setVisibility(View.VISIBLE);
                myViewHolder.img_favorite.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private TextView tv_phone;
        private TextView txtSTT;
        private TextView txtDanhgia;
        private RatingBar ratingBar;
        ImageView img;
        ImageView img_favorite, img_un_favorite;
        CircleImageView img_rank;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingbar_item);
            tv_name=(TextView)itemView.findViewById(R.id.books_name);
            tv_phone=(TextView)itemView.findViewById(R.id.books_chitiet);
//            txtSTT=(TextView)itemView.findViewById(R.id.txtSTT);
            txtDanhgia=(TextView)itemView.findViewById(R.id.txtDanhgia);
            img=(ImageView) itemView.findViewById(R.id.img_contact);
            img_favorite=(ImageView)itemView.findViewById(R.id.img_favorite);
            img_un_favorite=(ImageView)itemView.findViewById(R.id.img_un_favorite);
            img_rank = (CircleImageView)itemView.findViewById(R.id.img_rank);
        }
    }
}
