package iron2014.bansachonline.adapter;

import android.content.Context;
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
import iron2014.bansachonline.model.TheLoai;

public class TheLoaiAdapter extends RecyclerView.Adapter<TheLoaiAdapter.MyViewHolder> {

    Context context;
    List<TheLoai> mData;

    public TheLoaiAdapter(Context context, List<TheLoai> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_theloai, viewGroup, false);

        final MyViewHolder viewHolder= new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_name.setText(mData.get(i).getTenloai());
        String urlImage = mData.get(i).getImage();
        Picasso.with(context).load(urlImage).into(myViewHolder.img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private TextView tv_mota;
        ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name=(TextView)itemView.findViewById(R.id.tvname);
            tv_mota=(TextView)itemView.findViewById(R.id.tvMota);
            img=(ImageView) itemView.findViewById(R.id.img_theloai);
        }
    }

}
