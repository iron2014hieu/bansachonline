package iron2014.bansachonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import iron2014.bansachonline.R;
import iron2014.bansachonline.model.CTHD;

public class NhanxetAdapter extends RecyclerView.Adapter<NhanxetAdapter.MyViewHolder> {

    Context context;
    List<CTHD> mData;

    public NhanxetAdapter(Context context, List<CTHD> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_nhanxet, viewGroup, false);

        final MyViewHolder viewHolder= new MyViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String diem = String.valueOf(mData.get(i).getDiemdanhgia());
x
        myViewHolder.txtLoinhanxet_item.setText(mData.get(i).getNoidungdanhgia());
        myViewHolder.ratingbar_cuaban_item.setRating(Float.valueOf(diem));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout item_comment;
        private TextView txtNameuser_item;
        private TextView txtLoinhanxet_item;
        RatingBar ratingbar_cuaban_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_comment=(LinearLayout)itemView.findViewById(R.id.linearlayoutnx);
            txtNameuser_item=(TextView)itemView.findViewById(R.id.txtNameuser_item);
            txtLoinhanxet_item=(TextView)itemView.findViewById(R.id.txtLoinhanxet_item);
            ratingbar_cuaban_item=(RatingBar) itemView.findViewById(R.id.ratingbar_cuaban_item);
        }
    }
}
