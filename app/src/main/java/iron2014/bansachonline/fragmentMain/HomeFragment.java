package iron2014.bansachonline.fragmentMain;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import iron2014.bansachonline.Activity.BookDetailActivity;
import iron2014.bansachonline.Activity.GetAllBookActivity;
import iron2014.bansachonline.Activity.GetBookByTheloaiActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFace;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceNXB;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceTacgia;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceTheloai;
import iron2014.bansachonline.R;
import iron2014.bansachonline.RecycerViewTouch.RecyclerTouchListener;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.URL.UrlSql;
import iron2014.bansachonline.adapter.NhaxuatbanAdapter;
import iron2014.bansachonline.adapter.Sach.SachAdapter;
import iron2014.bansachonline.adapter.Slider.SliderAdapterExample;
import iron2014.bansachonline.adapter.TacgiaAdapter;
import iron2014.bansachonline.adapter.TheLoaiAdapter;
import iron2014.bansachonline.model.Books;
import iron2014.bansachonline.model.Nhaxuatban;
import iron2014.bansachonline.model.Tacgia;
import iron2014.bansachonline.model.TheLoai;
import retrofit2.Call;
import retrofit2.Callback;
public class HomeFragment extends Fragment {
    UrlSql urlSql = new UrlSql();
    SliderView sliderView;
    TextView tvXemThem;
    TheLoaiAdapter theLoaiAdapter;
    SachAdapter sachAdapter;
    TacgiaAdapter tacgiaAdapter;
    ProgressBar progressBar;
    NhaxuatbanAdapter nhaxuatbanAdapter;
    private List<TheLoai> listTheloai = new ArrayList<>();
    private List<Books> listBookhome = new ArrayList<>();
    private List<Nhaxuatban> listNhaxuatbanHome = new ArrayList<>();
    private List<Tacgia> listTacgia = new ArrayList<>();
    private List<Books> listSuggest = new ArrayList<>();

    private RecyclerView recyclerview_book_home;
    private RecyclerView recyclerview_nxb_home;
    private RecyclerView recyclerViewTheloai;
    private RecyclerView recyclerViewTacgia;
    private RecyclerView recyclerview_book_suggest;

    private ApiInTerFaceNXB apiInTerFaceNXB;
    private ApiInTerFaceTacgia apiInTerFaceTacgia;
    private ApiInTerFaceTheloai apiInTerFaceTheloai;
    private ApiInTerFace apiInTerFace;
    ImageButton buttonRecord;
    View view;
    String suggesst;
    SessionManager sessionManager;
    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        addControls();

        tvXemThem = view.findViewById(R.id.tvXemthem);
        tvXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), GetAllBookActivity.class);
                startActivity(i);
            }
        });
        sessionManager = new SessionManager(getContext());
        sachAdapter = new SachAdapter(getContext(), listBookhome);
        nhaxuatbanAdapter = new NhaxuatbanAdapter(getContext(), listNhaxuatbanHome);
        tacgiaAdapter = new TacgiaAdapter(getContext(), listTacgia);

        HashMap<String,String> sugess = sessionManager.getSuggest();
        suggesst = sugess.get(sessionManager.SUGGEST_BOOK);

        final SliderAdapterExample adapter = new SliderAdapterExample(getContext());
        adapter.setCount(5);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });

        recyclerview_book_home=view.findViewById(R.id.recyclerview_book_home);
        progressBar = view.findViewById(R.id.progress);
        buttonRecord =view.findViewById(R.id.buttonSpeech);

        buttonRecord.setVisibility(View.GONE);

        theLoaiAdapter = new TheLoaiAdapter(getContext(), listTheloai);
        // lấy sách
        StaggeredGridLayoutManager gridLayoutManagerVeticl =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerview_book_home.setLayoutManager(gridLayoutManagerVeticl);
        recyclerview_book_home.setHasFixedSize(true);
        // the loaij sachs
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerViewTheloai.setLayoutManager(gridLayoutManager);
        recyclerViewTheloai.setHasFixedSize(true);
        // recyclerview nhà xuất bản
        StaggeredGridLayoutManager gridLayoutManager3 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerview_nxb_home.setLayoutManager(gridLayoutManager3);
        recyclerview_nxb_home.setHasFixedSize(true);
        // tac gia sachs
        StaggeredGridLayoutManager gridLayoutManager4 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerViewTacgia.setLayoutManager(gridLayoutManager4);
        recyclerViewTacgia.setHasFixedSize(true);

        //recyclerview_book_suggest
        StaggeredGridLayoutManager gridLayoutManager5 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerview_book_suggest.setLayoutManager(gridLayoutManager5);
        recyclerview_book_suggest.setHasFixedSize(true);
        //book get by thể loại
        recyclerViewTheloai.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerViewTheloai, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TheLoai theloai =   listTheloai.get(position);
                String id = String.valueOf(theloai.getMaloai());
                String ten = theloai.getTenloai();
                sessionManager.createSessionGuimatheloai(id,ten);
                Intent intent = new Intent(getContext(), GetBookByTheloaiActivity.class);
                intent.putExtra("matheloai", id);
                intent.putExtra("tentheloai", ten);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
                TheLoai theloai =   listTheloai.get(position);

            }
        }));
        //book get by tác giả
        recyclerViewTacgia.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerViewTacgia, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Tacgia theloai =   listTacgia.get(position);
                String id = String.valueOf(theloai.getMatacgia());
                String ten = theloai.getTentacgia();

                sessionManager.createSessionGuimatacgia(id,ten);

                Intent intent = new Intent(getContext(), GetBookByTheloaiActivity.class);
                intent.putExtra("matacgia", id);
                intent.putExtra("tentacgia", ten);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        //book get by nhà xuất bản
        recyclerview_nxb_home.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerview_nxb_home, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Nhaxuatban theloai =   listNhaxuatbanHome.get(position);
                String id = String.valueOf(theloai.getManxb());
                String ten = theloai.getTennxb();

                sessionManager.createSessionGuimaNXB(id,ten);
                Intent intent = new Intent(getContext(), GetBookByTheloaiActivity.class);
                intent.putExtra("manxb", id);
                intent.putExtra("tennxb", ten);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        recyclerview_book_home.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerview_book_home, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Books books =   listBookhome.get(position);
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
                startActivity(new Intent(getContext(), BookDetailActivity.class));
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerview_book_suggest.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerview_book_suggest, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Books books =   listSuggest.get(position);
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
                startActivity(new Intent(getContext(), BookDetailActivity.class));
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchTheloai();
        fetchUser("");
        fetchNXB();
        fetchTacgia();
        fetchBookSuggest(suggesst);
        return view;
    }
    public void fetchTheloai(){
        apiInTerFaceTheloai = ApiClient.getApiClient().create(ApiInTerFaceTheloai.class);
        Call<List<TheLoai>> call = apiInTerFaceTheloai.getTheloai();

        call.enqueue(new Callback<List<TheLoai>>() {
            @Override
            public void onResponse(Call<List<TheLoai>> call, retrofit2.Response<List<TheLoai>> response) {
                progressBar.setVisibility(View.GONE);
                listTheloai= response.body();
                theLoaiAdapter = new TheLoaiAdapter(getContext(),listTheloai);
                recyclerViewTheloai.setAdapter(theLoaiAdapter);
                theLoaiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<TheLoai>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }

    public void fetchUser(String key){
        apiInTerFace = ApiClient.getApiClient().create(ApiInTerFace.class);
        Call<List<Books>> call = apiInTerFace.getBook_tensach(key);

        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, retrofit2.Response<List<Books>> response) {
                progressBar.setVisibility(View.GONE);
                listBookhome= response.body();
                sachAdapter = new SachAdapter(getContext(),listBookhome);
                recyclerview_book_home.setAdapter(sachAdapter);
                sachAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    public void fetchBookSuggest(String key){
        apiInTerFace = ApiClient.getApiClient().create(ApiInTerFace.class);
        Call<List<Books>> call = apiInTerFace.getBookSuggest(key);

        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, retrofit2.Response<List<Books>> response) {
                progressBar.setVisibility(View.GONE);
                listSuggest= response.body();
                sachAdapter = new SachAdapter(getContext(),listSuggest);
                recyclerview_book_suggest.setAdapter(sachAdapter);
                sachAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    public void fetchNXB(){
        apiInTerFaceNXB = ApiClient.getApiClient().create(ApiInTerFaceNXB.class);
        Call<List<Nhaxuatban>> call = apiInTerFaceNXB.getNXB();

        call.enqueue(new Callback<List<Nhaxuatban>>() {
            @Override
            public void onResponse(Call<List<Nhaxuatban>> call, retrofit2.Response<List<Nhaxuatban>> response) {
                progressBar.setVisibility(View.GONE);
                listNhaxuatbanHome= response.body();
                nhaxuatbanAdapter = new NhaxuatbanAdapter(getContext(),listNhaxuatbanHome);
                recyclerview_nxb_home.setAdapter(nhaxuatbanAdapter);
                nhaxuatbanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Nhaxuatban>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }
    public void fetchTacgia(){
        apiInTerFaceTacgia = ApiClient.getApiClient().create(ApiInTerFaceTacgia.class);
        Call<List<Tacgia>> call = apiInTerFaceTacgia.getTacgia();

        call.enqueue(new Callback<List<Tacgia>>() {
            @Override
            public void onResponse(Call<List<Tacgia>> call, retrofit2.Response<List<Tacgia>> response) {
                progressBar.setVisibility(View.GONE);
                listTacgia= response.body();
                tacgiaAdapter = new TacgiaAdapter(getContext(),listTacgia);
                recyclerViewTacgia.setAdapter(tacgiaAdapter);
                tacgiaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Tacgia>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Error Search:","Error on: "+t.toString());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    private void addControls(){
        recyclerViewTheloai = view.findViewById(R.id.recyclerview_theloai);
        sliderView = view.findViewById(R.id.imageSlider);
        recyclerview_nxb_home = view.findViewById(R.id.recyclerview_nxb_home);
        recyclerViewTacgia = view.findViewById(R.id.recyclerview_tacgia_home);
        recyclerview_book_suggest=view.findViewById(R.id.recyclerview_book_suggest);
    }
}
