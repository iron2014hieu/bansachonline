package iron2014.bansachonline.Activity.AppChatActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;

public class Adaptertinnhan extends RecyclerView.Adapter<Adaptertinnhan.viewHolder> {
  public static final int phai = 0;
   public static  final int trai = 1;


    Context context ;
    List<Modeltinnhan> modeltinnhanList;
    SessionManager sessionManager;
    String email;

    public Adaptertinnhan(Context context, List<Modeltinnhan> modeltinnhanList) {
        this.context = context;
        this.modeltinnhanList = modeltinnhanList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == phai){
          View view = LayoutInflater.from(context).inflate(R.layout.item_bentraichat,viewGroup,false);
          return new Adaptertinnhan.viewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_benphaichat,viewGroup,false);
            return new Adaptertinnhan.viewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        sessionManager = new SessionManager(context);
        HashMap<String,String> user = sessionManager.getUserDetail();
        email= user.get(sessionManager.EMAIL);

        holder.txt_noidung.setText(modeltinnhanList.get(position).getNoidung());
        holder.txt_emaill.setText(modeltinnhanList.get(position).getSender());
    }
    @Override
    public int getItemCount() {
        return modeltinnhanList.size();
    }

    public void insert(String conversation, int itemCount) {
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txt_noidung,txt_emaill;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txt_noidung = itemView.findViewById(R.id.txt_noidung);
            txt_emaill = itemView.findViewById(R.id.txt_emaill);
        }

    }

//    @Override
//    public int getItemViewType(int position) {
//        if (modeltinnhanList.get(position).getSender().equals(email)){
//            return phai;
//        }else {
//            return trai;
//        }
//    }
}
