package iron2014.bansachonline.adapter;

import android.app.Dialog;
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

import de.hdodenhof.circleimageview.CircleImageView;
import iron2014.bansachonline.R;
import iron2014.bansachonline.model.Users;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    Context context;
    List<Users> mData;
    Dialog myDialog;

    public UsersAdapter(Context context, List<Users> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_qltk, viewGroup, false);

        final MyViewHolder viewHolder= new MyViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv_name.setText(mData.get(i).getName());
        myViewHolder.tv_email.setText(mData.get(i).getEmail());
        myViewHolder.txtAddress_iv.setText(mData.get(i).getAddress());
        String urlImage = mData.get(i).getPhoto();
        String quyen = mData.get(i).getQuyen();
        Picasso.with(context).load(urlImage).into(myViewHolder.img_profile) ;

        if (quyen.equals("user")){
            myViewHolder.image_quyen.setImageResource(R.drawable.ic_person_outline);
        }else if (quyen.equals("store")){
            myViewHolder.image_quyen.setImageResource(R.drawable.ic_store);
        }else if (quyen.equals("admin")){
            myViewHolder.image_quyen.setImageResource(R.drawable.ic_supervisor_account);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private TextView tv_email;
        private TextView txtAddress_iv;
        private CircleImageView img_profile;
        private ImageView image_quyen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name=(TextView)itemView.findViewById(R.id.txtName_iv);
            tv_email=(TextView)itemView.findViewById(R.id.txtEmail_iv);
            txtAddress_iv=(TextView)itemView.findViewById(R.id.txtAddress_iv);
            img_profile=(CircleImageView) itemView.findViewById(R.id.img_user_profile_iv);
            image_quyen=(ImageView)itemView.findViewById(R.id.image_quyen);
        }
    }
}
