package iron2014.bansachonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iron2014.bansachonline.Activity.BookDetailActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFace;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceFav;
import iron2014.bansachonline.LoginRegister.ProfileActivity;
import iron2014.bansachonline.LoginRegister.UpdateProfileActivity;
import iron2014.bansachonline.RecycerViewTouch.RecyclerTouchListener;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.URL.UrlSql;
import iron2014.bansachonline.adapter.Sach.FavoriteAdapter;
import iron2014.bansachonline.model.BookFavorite;
import iron2014.bansachonline.model.Books;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;
import retrofit2.Call;
import retrofit2.Callback;

public class BookFavoriteActivity extends AppCompatActivity {
    RecyclerView recyclerview_fav;
    ApiInTerFaceFav apiInTerFaceFav;
    FavoriteAdapter favoriteAdapter;
    List<BookFavorite> listFav = new ArrayList<>();
    SessionManager sessionManager;
    TextView txtFav_empty;
    ApiInTerFace apiInTerFace;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_book_favorite);
        txtFav_empty = findViewById(R.id.txtFav_empty);

        recyclerview_fav = findViewById(R.id.recyclerview_fav);
        sessionManager= new SessionManager(this);

        StaggeredGridLayoutManager gridLayoutManager4 =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerview_fav.setLayoutManager(gridLayoutManager4);
        recyclerview_fav.setHasFixedSize(true);
        HashMap<String,String> user = sessionManager.getUserDetail();
        String mauser = user.get(sessionManager.ID);


        recyclerview_fav.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerview_fav, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                BookFavorite books = listFav.get(position);
                String masach = String.valueOf(books.getMasach());
                fectchBookCT(masach);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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
    public void fectchBookCT(String masach){
        apiInTerFace = ApiClient.getApiClient().create(ApiInTerFace.class);
        Call<List<Books>> call = apiInTerFace.getBookbyMasach(masach);

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

                    Intent intent =(new Intent(getBaseContext(), BookDetailActivity.class));

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
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
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
}
