package iron2014.bansachonline.Activity.AppChatActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import iron2014.bansachonline.R;

public class Adapterchat extends RecyclerView.Adapter<Adapterchat.viewHolder>{

    Context context ;
    List <Modelchat> modelchatList;

    public Adapterchat(Context context, List<Modelchat> modelchatList) {
        this.context = context;
        this.modelchatList = modelchatList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View layout;
        layout = LayoutInflater.from(context).inflate(R.layout.item_chatuser,viewGroup,false);
        return new viewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        holder.txtnguoidung.setText(modelchatList.get(position).getTennguoidung());
        holder.noidungchat.setText(modelchatList.get(position).getNoidungchat());
        holder.btnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,HienthiTnActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelchatList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtnguoidung, noidungchat;
        LinearLayout btnclick;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtnguoidung = itemView.findViewById(R.id.txt_tenchat);
            noidungchat = itemView.findViewById(R.id.txt_chuachat);
            btnclick = itemView.findViewById(R.id.btnclickchat);

        }
    }
}
