package iron2014.bansachonline.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFace;
import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.RecycerViewTouch.RecyclerTouchListener;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.Sach.SachAdapter;
import iron2014.bansachonline.model.Books;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;
import retrofit2.Call;
import retrofit2.Callback;

public class GetAllBookActivity extends AppCompatActivity {
    private RecyclerView recyclerview_book_all;
    private ApiInTerFace apiInTerFace;
    SachAdapter sachAdapter;
    private List<Books> listBook = new ArrayList<>();
    SessionManager sessionManager;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_get_all_book);
        recyclerview_book_all=findViewById(R.id.recyclerview_book_all);

        sachAdapter = new SachAdapter(this, listBook);
        sessionManager = new SessionManager(this);
        Toolbar toolbar = findViewById(R.id.toolbargh);
        ActionBar actionBar = getSupportActionBar();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();

        // lấy sách
        StaggeredGridLayoutManager gridLayoutManagerVeticl =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_book_all.setLayoutManager(gridLayoutManagerVeticl);
        recyclerview_book_all.setHasFixedSize(true);


        fetchBookall();

        recyclerview_book_all.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerview_book_all, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Books books =   listBook.get(position);
                String masach = String.valueOf(books.getMasach());
                String tensach = String.valueOf(books.getTensach());
                String manxb = String.valueOf(books.getManxb());
                String matheloai = String.valueOf(books.getMatheloai());
                String ngayxb = books.getNgayxb();
                String noidung = books.getNoidung();
                String anhbia =books.getAnhbia();
                String gia = String.valueOf( books.getGia());
                String tennxb= String.valueOf(books.getTennxb());
                String soluong = String.valueOf(books.getSoluong());
                String tacgia = books.getTacgia();
                String matacgia = String.valueOf(books.getMatacgia());


                String tongdiem = String.valueOf(books.getTongdiem());
                String landanhgia = String.valueOf(books.getLandanhgia());

                sessionManager.createSessionSendInfomationBook(masach,tensach,manxb,matheloai,ngayxb,noidung,
                        anhbia,gia,tennxb,soluong,tacgia,matacgia, tongdiem, landanhgia);
                startActivity(new Intent(GetAllBookActivity.this, BookDetailActivity.class));
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
    //settheme
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
    public void fetchBookall() {
        apiInTerFace = ApiClient.getApiClient().create(ApiInTerFace.class);
        Call<List<Books>> call = apiInTerFace.getBookDESC();

        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, retrofit2.Response<List<Books>> response) {
                //progressBar.setVisibility(View.GONE);
                listBook= response.body();
                sachAdapter = new SachAdapter(GetAllBookActivity.this,listBook);
                recyclerview_book_all.setAdapter(sachAdapter);
                sachAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
