package iron2014.bansachonline.adapter.hoadoncthd;

import android.content.Context;
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

import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.model.CTHD;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.MyViewHolder> {

    Context context;
    List<CTHD> mData;
    private ProductItemActionListener actionListener;
    SessionManager sessionManager;
    public LibraryAdapter(Context context, List<CTHD> mData) {
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
        view = LayoutInflater.from(context).inflate(R.layout.item_thuvien, viewGroup, false);
        sessionManager = new SessionManager(context);
        final MyViewHolder viewHolder= new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        String diem = String.valueOf(mData.get(i).getDiemdanhgia());
        holder.tv_name_book_lib.setText(mData.get(i).getTensach());
        holder.tv_tacgia_book_lib.setRating(Float.valueOf(diem));
        try {
            Picasso.with(context).load(mData.get(i).getHinhanh()).into(holder.img_book_lib);
        }catch (Exception e){}

    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name_book_lib;
        private RatingBar tv_tacgia_book_lib;
        private ImageView img_book_lib;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tacgia_book_lib = (RatingBar) itemView.findViewById(R.id.tv_tacgia_book_lib);
            tv_name_book_lib=(TextView)itemView.findViewById(R.id.tv_name_book_lib);
            img_book_lib=(ImageView) itemView.findViewById(R.id.img_book_lib);

        }
    }
    public interface ProductItemActionListener{
        void onItemTap(ImageView imageView);
    }
}
