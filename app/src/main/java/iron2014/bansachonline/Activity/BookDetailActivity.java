package iron2014.bansachonline.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iron2014.bansachonline.Activity.AppChatActivity.HienthiTnActivity;
import iron2014.bansachonline.Activity.hoadon.ListAllCommentActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFace;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceFav;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceHoadon;
import iron2014.bansachonline.BottomSheet.ExampleBottomSheetDialog;
import iron2014.bansachonline.Main2Activity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.URL.UrlSql;
import iron2014.bansachonline.adapter.NhanxetAdapter;
import iron2014.bansachonline.adapter.Sach.SachAdapter;
import iron2014.bansachonline.model.BookFavorite;
import iron2014.bansachonline.model.Books;
import iron2014.bansachonline.model.CTHD;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;
import retrofit2.Call;
import retrofit2.Callback;

public class BookDetailActivity extends AppCompatActivity implements ExampleBottomSheetDialog.BottomSheetListener{
    SharedPref sharedPref;
    Button btn_themgh,btn_muangay;
    private ImageView img_book;
    private TextView edtTensach, edtGiaban,edtMotaChitiet,txt_numrating_below_deatil,txt_numrating_book_detail, textNotify;
    private TextView txtXemtataca;
    private RatingBar ratingbar_book_detail, ratingbar_below_detail;
    private ImageButton btn_Share,btn_Message;
    LinearLayout linnear_nhanxet;
    ApiInTerFaceFav apiInTerFaceFav;
    private String URL_INSERT ="http://hieuttpk808.000webhostapp.com/books/cart_bill/insert.php";
    private String URL_CHECK ="https://hieuttpk808.000webhostapp.com/books/cart_bill/checklibrary.php";

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    String idUser, name, quyen;
    private Double giabansach = 0.0;
    private Float diemdanhgia;
    private String idBook, tensach,mota,hinhanh, giaban,
            soluong, landanhgia, tongdiem, linkImage, masach,matacgia;
    SessionManager sessionManager;
    RecyclerView recyclerview_nhanxet,recyclerview_sach_tacgia;
    List<CTHD> listNhanxet = new ArrayList<>();
    List<Books> listBooks = new ArrayList<>();
    SachAdapter sachAdapter;
    NhanxetAdapter nhanxetAdapter;
    ApiInTerFaceHoadon apiInTerFaceHoadon;
    ApiInTerFace apiInTerFace;
    ImageButton img_like,img_unlike,btn_Share_fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        theme();
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_book_detail);
        addcontrols();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar_sach_chitiet);
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

        sessionManager = new SessionManager(BookDetailActivity.this);

        HashMap<String,String> book = sessionManager.getBookDetail();
        idBook = book.get(sessionManager.MASACH);
        mota=book.get(sessionManager.NOIDUNG);
        giaban = (book.get(sessionManager.GIA));
        soluong = (book.get(sessionManager.SOLUONG));
        tensach = book.get(sessionManager.TENSACH);
        masach = book.get(sessionManager.MASACH);
        matacgia = book.get(sessionManager.MATACGIA);
        toolbar.setTitle(tensach);

        Log.d("Masach", masach);
        tongdiem= (book.get(sessionManager.TONGDIEM));
        landanhgia=book.get(sessionManager.LANDANHGIA);
        linkImage = book.get(sessionManager.ANHBIA);
        Picasso.with(this)
                .load(linkImage).into(img_book);
            HashMap<String,String> user = sessionManager.getUserDetail();
            quyen = user.get(sessionManager.QUYEN);
            name = user.get(sessionManager.NAME);
            idUser = user.get(sessionManager.ID);

            if (quyen.equals("user")){
                CheckLibrary(idBook, idUser);
            }

        if (Float.valueOf(tongdiem) == 0.0)
        {
            ratingbar_book_detail.setVisibility(View.GONE);
            ratingbar_book_detail.setVisibility(View.GONE);
            txt_numrating_below_deatil.setVisibility(View.GONE);
            txt_numrating_below_deatil.setVisibility(View.GONE);
            linnear_nhanxet.setVisibility(View.GONE);
        }else {
            diemdanhgia =(Float.parseFloat(tongdiem)/Float.parseFloat(landanhgia));
            ratingbar_book_detail.setRating(diemdanhgia);
            ratingbar_below_detail.setRating(diemdanhgia);

            txt_numrating_book_detail.setText(""+diemdanhgia);
            txt_numrating_below_deatil.setText(diemdanhgia+" ("+landanhgia+" đánh giá)");
        }

        edtTensach.setText(tensach);
        edtGiaban.setText(giaban+" VNĐ");
        edtMotaChitiet.setText(mota);

        giabansach = Double.valueOf(giaban);
        StaggeredGridLayoutManager gridLayoutManagerVeticl =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_nhanxet.setLayoutManager(gridLayoutManagerVeticl);
        recyclerview_nhanxet.setHasFixedSize(true);
        //lay sach cung tac gia
        StaggeredGridLayoutManager gridLayoutManagerVeticl1 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerview_sach_tacgia.setLayoutManager(gridLayoutManagerVeticl1);
        recyclerview_sach_tacgia.setHasFixedSize(true);
        //Toast.makeText(this, "Lấy dữ liệu "+matacgia, Toast.LENGTH_SHORT).show();
        fetchNhanxet(masach);
        fetchSach_tacgia(matacgia);

        btn_muangay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExampleBottomSheetDialog bottomSheet = new ExampleBottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
//                    HashMap<String,String> user = sessionManager.getUserDetail();
//                    idUser = user.get(sessionManager.ID);
//                    if (idUser==null){
//                        Toast.makeText(BookDetailActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(BookDetailActivity.this, LoginActivity.class));
//                    }else {
//                        ThemDatmua(masach, tensach, linkImage,"5",idUser);
//                        //final String masach, final String sp, final String hinhanhsach, final String mauser
//                    }
            }
        });
        //share text
        // sharing intent
        //share all
        btn_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "https://play.google.com/store/audiobooks/details/Max_Gladstone_Bookburners_The_Complete_Season_2?id=AQAAAEDMAm1CsM&hl=vi";
                // sharing intent
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Viết gì đó");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(sharingIntent, "Chia sẻ"));
            }
        });




        //share fb api
        btn_Share_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                            .setShareHashtag((new ShareHashtag.Builder()
                                    .setHashtag("#ConnectTheWorld")
                                    .build()))
                            .build();
                    shareDialog.show(linkContent);
                }
            }
        });

        btn_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(getBaseContext(), HienthiTnActivity.class);
                startActivity(o);
            }
        });
        txtXemtataca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ListAllCommentActivity.class);
                startActivity(intent);
            }
        });

        checkLike(masach, idUser);
        img_unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemYeuthich();
                img_unlike.setVisibility(View.GONE);
                img_like.setVisibility(View.VISIBLE);
            }
        });
        img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoYeuthich();
                img_like.setVisibility(View.GONE);
                img_unlike.setVisibility(View.VISIBLE);
            }
        });
    }

    private void ThemDatmua(final String masach, final String sp, final String hinhanhsach,final String soluongdat, final String mauser){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_INSERT_GIOHANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String check = jsonObject.getString("check");

                            if(check.equals("chuatontai")){

                                if (success.equals("1")){
                                  Toast.makeText(BookDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("printStackTrace", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookDetailActivity.this, "Loi roi nhe", Toast.LENGTH_SHORT).show();
                Log.d("MYSQL", "Lỗi! \n" +error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("masach", masach);
                params.put("sanpham", sp);
                params.put("hinhanh", hinhanhsach);
                params.put("gia", giaban);
                params.put("soluong", soluongdat);
                params.put("mauser", mauser);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void ThemYeuthich(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_INSERT_FAVORITE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")){
                                Toast.makeText(BookDetailActivity.this, "Đã thêm yêu thích", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("printStackTrace", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MYSQL", "Lỗi! \n" +error.toString());
                Toast.makeText(BookDetailActivity.this, "loi: "+error, Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("masach", idBook);
                params.put("mauser", idUser);
                params.put("tensach", tensach);
                params.put("anhbia", hinhanh);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void BoYeuthich(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSql.URL_DELETE_FAVORITE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")){
                                Toast.makeText(BookDetailActivity.this, "Đã bỏ thích", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("printStackTrace", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MYSQL", "Lỗi! \n" +error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("masach", idBook);
                params.put("mauser", idUser);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void CheckLibrary(final String idBook, final String idUser){
        RequestQueue requestQueue = Volley.newRequestQueue(BookDetailActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("datontai")){
                            Toast.makeText(BookDetailActivity.this, "Bạn đã mua sách này", Toast.LENGTH_SHORT).show();
                        }else {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MYSQL", "Lỗi! \n" +error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("masach", idBook);
                params.put("mauser", idUser);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void fetchNhanxet(String key){
        apiInTerFaceHoadon = ApiClient.getApiClient().create(ApiInTerFaceHoadon.class);
        Call<List<CTHD>> call = apiInTerFaceHoadon.get_5_cthd(key);

        call.enqueue(new Callback<List<CTHD>>() {
            @Override
            public void onResponse(Call<List<CTHD>> call, retrofit2.Response<List<CTHD>> response) {
                //progressBar.setVisibility(View.GONE);
                listNhanxet= response.body();
                nhanxetAdapter = new NhanxetAdapter(BookDetailActivity.this,listNhanxet);
                recyclerview_nhanxet.setAdapter(nhanxetAdapter);
                nhanxetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CTHD>> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    private void fetchSach_tacgia(String matacgia){
        apiInTerFace = ApiClient.getApiClient().create(ApiInTerFace.class);
        Call<List<Books>> call = apiInTerFace.getBookbyMatacgia(matacgia);

        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, retrofit2.Response<List<Books>> response) {
                listBooks= response.body();
                sachAdapter = new SachAdapter(BookDetailActivity.this,listBooks);
                recyclerview_sach_tacgia.setAdapter(sachAdapter);
                sachAdapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void checkLike(String masach, String mauser){
        apiInTerFaceFav = ApiClient.getApiClient().create(ApiInTerFaceFav.class);
        Call<List<BookFavorite>> call = apiInTerFaceFav.check_like(masach, mauser);

        call.enqueue(new Callback<List<BookFavorite>>() {
            @Override
            public void onResponse(Call<List<BookFavorite>> call, retrofit2.Response<List<BookFavorite>> response) {
                if (response.body().size()==0){
                    img_like.setVisibility(View.GONE);
                    img_unlike.setVisibility(View.VISIBLE);
                }else {
                    img_like.setVisibility(View.VISIBLE);
                    img_unlike.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<BookFavorite>> call, Throwable t) {
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    @Override
    public void onButtonClicked(String text) {
        ThemDatmua(masach, tensach, linkImage,text,idUser);
        Toast.makeText(this, ""+text, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Main2Activity.class));
    }
    private void addcontrols(){
        btn_Message = findViewById(R.id.btn_chat);
        edtTensach = findViewById(R.id.edtTensach);
        edtGiaban=findViewById(R.id.edtGiaban);
        edtMotaChitiet=findViewById(R.id.edtMotaChitiet);
        btn_muangay=findViewById(R.id.btn_muangay);
        img_book=findViewById(R.id.imgBook);
        btn_Share= findViewById(R.id.btn_Share);
        ratingbar_below_detail = findViewById(R.id.ratingbar_below_detail);
        ratingbar_book_detail = findViewById(R.id.ratingbar_book_detail);
        txt_numrating_below_deatil = findViewById(R.id.numrating_below_deatil);
        txt_numrating_book_detail = findViewById(R.id.numrating_book_detail);
        recyclerview_nhanxet = findViewById(R.id.recyclerview_nhanxet);
        linnear_nhanxet=findViewById(R.id.linnear_nhanxet);
        recyclerview_sach_tacgia= findViewById(R.id.recyclerview_sach_tacgia);
        txtXemtataca= findViewById(R.id.txtXemtataca);
        btn_Share_fb=findViewById(R.id.btn_Share_fb);

        img_like= findViewById(R.id.img_like);
        img_unlike = findViewById(R.id.img_unlike);
    }
}
