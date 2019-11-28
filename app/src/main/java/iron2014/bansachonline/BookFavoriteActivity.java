package iron2014.bansachonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFace;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceFav;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.Sach.FavoriteAdapter;
import iron2014.bansachonline.model.BookFavorite;
import iron2014.bansachonline.model.Books;
import retrofit2.Call;
import retrofit2.Callback;

public class BookFavoriteActivity extends AppCompatActivity {
    RecyclerView recyclerview_fav;
    ApiInTerFaceFav apiInTerFaceFav;
    FavoriteAdapter favoriteAdapter;
    List<BookFavorite> listFav = new ArrayList<>();
    SessionManager sessionManager;
    TextView txtFav_empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_favorite);
        txtFav_empty = findViewById(R.id.txtFav_empty);

        recyclerview_fav = findViewById(R.id.recyclerview_fav);
        sessionManager= new SessionManager(this);

        StaggeredGridLayoutManager gridLayoutManager4 =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_fav.setLayoutManager(gridLayoutManager4);
        recyclerview_fav.setHasFixedSize(true);
        HashMap<String,String> user = sessionManager.getUserDetail();
        String mauser = user.get(sessionManager.ID);

        fetchFav(mauser);
    }
    public void fetchFav(String mauser){
        apiInTerFaceFav = ApiClient.getApiClient().create(ApiInTerFaceFav.class);
        Call<List<BookFavorite>> call = apiInTerFaceFav.get_favorite(mauser);

        call.enqueue(new Callback<List<BookFavorite>>() {
            @Override
            public void onResponse(Call<List<BookFavorite>> call, retrofit2.Response<List<BookFavorite>> response) {
                listFav= response.body();
                favoriteAdapter = new FavoriteAdapter(getApplicationContext(),listFav);
                recyclerview_fav.setAdapter(favoriteAdapter);
                favoriteAdapter.notifyDataSetChanged();
                if (listFav.size() != 0){
                    txtFav_empty.setVisibility(View.GONE);
                }else {
                    txtFav_empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<BookFavorite>> call, Throwable t) {
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
}
