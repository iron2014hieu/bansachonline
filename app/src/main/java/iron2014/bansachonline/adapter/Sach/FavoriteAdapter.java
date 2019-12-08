package iron2014.bansachonline.adapter.Sach;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import iron2014.bansachonline.Activity.BookDetailActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFace;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.model.BookFavorite;
import iron2014.bansachonline.model.Books;
import retrofit2.Call;
import retrofit2.Callback;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    Context context;
    List<BookFavorite> mData;
    SessionManager sessionManager;
    ApiInTerFace apiInTerFace;

    public FavoriteAdapter(Context context, List<BookFavorite> mData) {
        this.context = context;
        this.mData = mData;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_favorite, viewGroup, false);

        final MyViewHolder viewHolder= new MyViewHolder(view);
        sessionManager = new SessionManager(context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.txt_namebook_favorite.setText(mData.get(i).getTensach());
        String linkImage= mData.get(i).getAnhbia();
        if (linkImage!=null) {
            Picasso.with(context).load(linkImage).into(myViewHolder.img_book_favorite);
        }
//        myViewHolder.img_book_favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fectchBookCT(mData.get(i).getTensach());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_namebook_favorite;
        private ImageView  img_book_favorite;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_book_favorite = itemView.findViewById(R.id.img_book_favorite);
            txt_namebook_favorite=itemView.findViewById(R.id.txt_namebook_favorite);
        }
    }
    public void fectchBookCT(String tensach){
        apiInTerFace = ApiClient.getApiClient().create(ApiInTerFace.class);
        Call<List<Books>> call = apiInTerFace.getBook_tensach(tensach);

        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, retrofit2.Response<List<Books>> response) {
                try {
                    Books books = response.body().get(0);
                    final String masach = String.valueOf(books.getMasach());
                    final String tensach = String.valueOf(books.getTensach());
                    final String manxb = String.valueOf(books.getManxb());
                    final String matheloai = String.valueOf(books.getMatheloai());
                    final String ngayxb = books.getNgayxb();
                    final String noidung = books.getNoidung();
                    final String anhbia =books.getAnhbia();
                    final String gia = String.valueOf( books.getGia());
                    final String tennxb= String.valueOf(books.getTennxb());
                    final String soluong = String.valueOf(books.getSoluong());
                    final String tacgia = books.getTacgia();
                    final String matacgia = String.valueOf(books.getMatacgia());
                    final String tongdiem = String.valueOf(books.getTongdiem());
                    final String landanhgia = String.valueOf(books.getLandanhgia());
                    sessionManager.createSessionSendInfomationBook(masach,tensach,manxb,matheloai,ngayxb,noidung,
                            anhbia,gia,tennxb,soluong,tacgia,matacgia, tongdiem, landanhgia);

                    Intent intent =(new Intent(context, BookDetailActivity.class));

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);
                }catch (Exception e){
                    Log.e("errdetailfav", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
}
