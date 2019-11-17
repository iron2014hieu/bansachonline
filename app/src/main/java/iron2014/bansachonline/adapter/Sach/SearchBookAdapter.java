package iron2014.bansachonline.adapter.Sach;

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
import iron2014.bansachonline.model.Books;

public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookAdapter.MyViewHolder>{
    private List<Books> users;
    private Context context;

    public SearchBookAdapter(List<Books> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sach, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(users.get(position).getTensach());
        holder.email.setText("d"+users.get(position).getGia());


        try {
            String urlImage = users.get(position).getAnhbia();
            if (urlImage==null){
                holder.img.setImageResource(R.drawable.book);
            }else {
                Picasso.with(context).load(urlImage).into(holder.img);
            }

        }catch (Exception e){
            Log.e("IMG", e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, email;
        ImageView img, favorite, un_favorite;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.books_name);
            email = itemView.findViewById(R.id.books_chitiet);
            img = itemView.findViewById(R.id.img_book_iv);
        }
    }
}
