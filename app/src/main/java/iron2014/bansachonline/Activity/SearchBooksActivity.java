package iron2014.bansachonline.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFace;
import iron2014.bansachonline.R;
import iron2014.bansachonline.RecycerViewTouch.RecyclerTouchListener;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.Sach.SachAdapter;
import iron2014.bansachonline.model.Books;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;
import retrofit2.Call;
import retrofit2.Callback;

public class SearchBooksActivity extends AppCompatActivity {
    MaterialSearchView searchView;
    ApiInTerFace apiInTerFace;
    private SachAdapter sachAdapter;
    private List<Books> listBookSearch = new ArrayList<>();

    ImageButton buttonRecord;
    RecyclerView recyclerview_book_search;
    SessionManager sessionManager;
    TextView txtSearch_null;
    ProgressBar progressBar;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);
        sharedPref = new SharedPref(this);
        theme();
        addControl();
//        checkPermission();
        sessionManager = new SessionManager(this);
        buttonRecord.setVisibility(View.GONE);
        Toolbar toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.RECORD_AUDIO)
                .withListener(new BaseMultiplePermissionsListener(){
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        super.onPermissionsChecked(report);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        super.onPermissionRationaleShouldBeShown(permissions, token);
                    }
                }).check();


        sachAdapter = new SachAdapter(SearchBooksActivity.this, listBookSearch);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_book_search.setLayoutManager(gridLayoutManager);
        recyclerview_book_search.setAdapter(sachAdapter);
        recyclerview_book_search.setHasFixedSize(true);

        fetchBookRandom("");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchBookRandom(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchBookRandom(newText);
                return false;
            }
        });
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    fetchBookRandom(query);
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    fetchBookRandom(newText);
//                    return false;
//                }
//            });


        recyclerview_book_search.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerview_book_search, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Books books =   listBookSearch.get(position);
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

                startActivity(new Intent(SearchBooksActivity.this, BookDetailActivity.class));
            }

            @Override
            public void onLongClick(View view, int position) {
                // code here
            }
        }));

        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
//        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
//            @Override
//            public boolean onSuggestionSelect(int position) {
//                return false;
//            }
//
//            @Override
//            public boolean onSuggestionClick(int position) {
//                return false;
//            }
//        });

        //speech to text
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        searchView.clearFocus();
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.clearFocus(); // close the keyboard on load
                buttonRecord.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                buttonRecord.setVisibility(View.GONE);
            }
        });

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    searchView.setQuery(String.valueOf(matches.get(0)), false);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        buttonRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
//                        searchView.setQueryHint("Nhập hoặc nói để tìm kiếm");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
//                        searchView.setQueryHint("");
                        break;
                }
                return false;
            }
        });
    }
    //settheme
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void fetchBookRandom(String key){
        apiInTerFace = ApiClient.getApiClient().create(ApiInTerFace.class);
        Call<List<Books>> call = apiInTerFace.getBookRandom(key);

        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, retrofit2.Response<List<Books>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().size() == 0){
                    txtSearch_null.setVisibility(View.VISIBLE);
                }else {
                    txtSearch_null.setVisibility(View.GONE);
                }
                listBookSearch= response.body();
                sachAdapter = new SachAdapter(SearchBooksActivity.this,listBookSearch);
                recyclerview_book_search.setAdapter(sachAdapter);
                sachAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.material_searchview, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()){
            searchView.closeSearch();
        }else
            super.onBackPressed();
    }
    private void addControl() {
        searchView = findViewById(R.id.search_view_all);
        buttonRecord =findViewById(R.id.buttonSpeech);
        recyclerview_book_search= findViewById(R.id.recyclerview_book_search);
        txtSearch_null=findViewById(R.id.txtSearch_null);
        progressBar=findViewById(R.id.progress_search);
    }
}
