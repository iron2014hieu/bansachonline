package iron2014.bansachonline.Activity.Library;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iron2014.bansachonline.Activity.BookDetailActivity;
import iron2014.bansachonline.Activity.hoadon.RatingBookCommentActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFace;
import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Service.App;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.Sach.SachAdapter;
import iron2014.bansachonline.model.Books;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;
import retrofit2.Call;
import retrofit2.Callback;

public class BookDetailLibActivity extends AppCompatActivity {
    String masach,idcthd, linkbook,hinhanh, tensach, tongdiem, landanhgia,audio,tacgia;
    RecyclerView recyclerview_sach_sachdexuat;
    ImageView imgBook_lib;
    TextView txtTensach_lib,numrating_book_detail_lib,txtDocsach,txtXemnhanxet,txtNgheaudio,txtDiem;
    RatingBar ratingbar_book_detail_lib;
    String URL ="https://bansachonline.xyz/bansach/sach/getBookDetail.php/?masach=";
    SessionManager sessionManager;
    SharedPref sharedPref;
    NotificationManagerCompat notificationManagerCompat;
    MediaSessionCompat mediaSessionCompat;
    RatingBar ratingbar_lib;
    TextView edtNhanxet_lib;
    ApiInTerFace apiInTerFace;
    List<Books> listBooks = new ArrayList<>();
    SachAdapter sachAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_book_detail_lib);
        addcontrols();
        Toolbar toolbar = findViewById(R.id.toolbar_lib);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        sessionManager = new SessionManager(this);
        final Intent intent = getIntent();
        masach = intent.getStringExtra("masach");
        tensach = intent.getStringExtra("tensach");
        Log.d("tensach_lib", tensach);
        toolbar.setTitle(tensach);
        getDetailBook(URL+masach);

        HashMap<String,String> commecn=sessionManager.getCTHD_ID();
        final String noidung = commecn.get(sessionManager.NOIDUNGCTHD);
        final String diem  = commecn.get(sessionManager.DIEMDANHGIACTHD);
        idcthd = commecn.get(sessionManager.IDCTHD);
        if (noidung==null){
            edtNhanxet_lib.setVisibility(View.GONE);
            ratingbar_lib.setVisibility(View.GONE);
            txtDiem.setVisibility(View.GONE);
            txtXemnhanxet.setText("Thêm đánh giá");

            txtXemnhanxet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentThemnx  = new Intent(getBaseContext(), RatingBookCommentActivity.class);
                    intentThemnx.putExtra("masach",masach);
                    intentThemnx.putExtra("idcthd", idcthd);
                    intentThemnx.putExtra("noidung", noidung);
                    intentThemnx.putExtra("diem", diem);
                    startActivity(intentThemnx);
                }
            });
        }else {
            edtNhanxet_lib.setText(noidung);
            ratingbar_lib.setRating(Float.valueOf(diem));
            txtDiem.setText(diem+"");
            txtXemnhanxet.setVisibility(View.GONE);
            txtXemnhanxet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentThemnx1  = new Intent(getBaseContext(), RatingBookCommentActivity.class);
                    intentThemnx1.putExtra("masach",masach);
                    intentThemnx1.putExtra("idcthd", idcthd);
                    startActivity(intentThemnx1);
                }
            });
        }


        notificationManagerCompat = NotificationManagerCompat.from(this);
        mediaSessionCompat = new MediaSessionCompat(this, "tag");

        Intent intent1 = new Intent(this, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        String check = "4";
        intent.putExtra("check", check);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                intent,
                0
        );
        mediaSessionCompat.setSessionActivity(pendingIntent);

        txtNgheaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
                Intent i = new Intent(getBaseContext(),AudioActivity.class);
                i.putExtra("masach", masach);
                startActivity(i);
            }
        });
        recyclerview_sach_sachdexuat = findViewById(R.id.recyclerview_sach_sachdexuat);
        StaggeredGridLayoutManager gridLayoutManagerVeticl2 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerview_sach_sachdexuat.setLayoutManager(gridLayoutManagerVeticl2);
        recyclerview_sach_sachdexuat.setHasFixedSize(true);
        fetchBookRandom();
    }
    public void fetchBookRandom(){
        apiInTerFace = ApiClient.getApiClient().create(ApiInTerFace.class);
        Call<List<Books>> call = apiInTerFace.getBookRandom("");

        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, retrofit2.Response<List<Books>> response) {
                listBooks= response.body();
                sachAdapter = new SachAdapter(BookDetailLibActivity.this,listBooks);
                recyclerview_sach_sachdexuat.setAdapter(sachAdapter);
                sachAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    private void sendNotification(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sach3);


        Intent intent = new Intent(this, AudioActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                Integer.parseInt(App.CHANNEL_ID_1),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        Notification channel = new NotificationCompat.Builder(getApplicationContext(), App.CHANNEL_ID_1)
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(tensach)
                .setContentText(tacgia)
                .setContentIntent(pendingIntent)
                .setLargeIcon(bitmap)
                .addAction(R.drawable.ic_fast_rewind, "xx10", null)
                .addAction(R.drawable.ic_skip_previous, "prev", null)
                .addAction(R.drawable.ic_pause_circle_outline, "next", null)
                .addAction(R.drawable.ic_skip_next, "xx10", null)
                .addAction(R.drawable.ic_fast_forward, "pause", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1,2,3)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setSubText("Sub text")
                .build();
        notificationManagerCompat.notify(1, channel);
    }
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }

    public void getDetailBook(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(BookDetailLibActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject object = response.getJSONObject(0);
                            tensach = object.getString("tensach");
                            linkbook = object.getString("linkbook");
                            hinhanh = object.getString("anhbia");
                            tongdiem = object.getString("tongdiem");
                            landanhgia = object.getString("landanhgia");
                            audio=object.getString("audio");
                            tacgia=object.getString("tacgia");

                            sessionManager.createGuiLinkBook(tensach,tacgia, linkbook,audio);
                            txtTensach_lib.setText(tensach);
                            Picasso.with(BookDetailLibActivity.this).load(hinhanh).into(imgBook_lib);
                            numrating_book_detail_lib.setText(landanhgia);
                            ratingbar_book_detail_lib.setRating(Float.valueOf(tongdiem)/Float.valueOf(landanhgia));
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(BookDetailLibActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookDetailLibActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_library, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.share_book:
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    public void DocSach(View view){
        Intent intent = new Intent(getBaseContext(), ViewBookActivity.class);
        startActivity(intent);
    }
    public void xemsachkhac(View view) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("check","2");
        startActivity(intent);
    }
    private void addcontrols() {
        imgBook_lib = findViewById(R.id.imgBook_lib_acti);
        txtTensach_lib = findViewById(R.id.txtTensach_lib);
        numrating_book_detail_lib= findViewById(R.id.numrating_book_detail_lib);
        txtDocsach= findViewById(R.id.txtDocsach);
        txtXemnhanxet= findViewById(R.id.txtXemnhanxet);
        ratingbar_book_detail_lib = findViewById(R.id.ratingbar_book_detail_lib);
        txtNgheaudio= findViewById(R.id.txtNgheaudio);
        edtNhanxet_lib = findViewById(R.id.edtNhanxet_lib);
        ratingbar_lib= findViewById(R.id.ratingbar_lib);
        txtDiem = findViewById(R.id.txtDiem);
    }
}