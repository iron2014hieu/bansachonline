package iron2014.bansachonline.adapter.Sach;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import iron2014.bansachonline.R;
import iron2014.bansachonline.model.Books;

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.MyViewHolder> {

    Context context;
    List<Books> mData;
    Dialog myDialog;
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


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv_name.setText(mData.get(i).getTensach());
        myViewHolder.tv_phone.setText("Giá: "+mData.get(i).getGia()+"₫");

//        myViewHolder.tv_soluongsach.setText(String.valueOf(mData.get(i).getSoluong()));

        myViewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionListener!=null)
                    actionListener.onItemTap(myViewHolder.img);
            }
        });
        try {
            String urlImage = mData.get(i).getAnhbia();
            if (urlImage==null){
                myViewHolder.img.setImageResource(R.drawable.profile2);
            }else {
                Picasso.with(context).load(urlImage).into(myViewHolder.img);
            }
        }catch (Exception e){
            Log.e("IMG", e.toString());
        }

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
        ImageView img, favorite;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name=(TextView)itemView.findViewById(R.id.books_name);
            tv_phone=(TextView)itemView.findViewById(R.id.books_chitiet);
            img=(ImageView) itemView.findViewById(R.id.img_book_iv);
            tv_sldaban=(TextView) itemView.findViewById(R.id.tv_soluongsach);
        }
    }
    public interface ProductItemActionListener{
        void onItemTap(ImageView imageView);
    }
}
