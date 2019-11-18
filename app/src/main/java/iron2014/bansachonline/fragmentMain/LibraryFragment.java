package iron2014.bansachonline.fragmentMain;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iron2014.bansachonline.Activity.Library.BookDetailLibActivity;
import iron2014.bansachonline.ApiRetrofit.ApiClient;
import iron2014.bansachonline.ApiRetrofit.InTerFace.ApiInTerFaceHoadon;
import iron2014.bansachonline.R;
import iron2014.bansachonline.RecycerViewTouch.RecyclerTouchListener;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.adapter.hoadoncthd.LibraryAdapter;
import iron2014.bansachonline.model.CTHD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {

    View view;
    private SessionManager sessionManager;
    private RecyclerView recyclerview_book_library;
    private ProgressBar progressBar;
    ApiInTerFaceHoadon apiInTerFaceHoadon;
    LibraryAdapter libraryAdapter;
    List<CTHD> listLibrary = new ArrayList<>();
    TextView txtLib_empty;
    public LibraryFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_library, container, false);
        recyclerview_book_library = view.findViewById(R.id.recyclerview_book_library);
        progressBar = view.findViewById(R.id.progress_lib);
        txtLib_empty=view.findViewById(R.id.txtLib_empty);
        sessionManager = new SessionManager(getContext());
        // the loaij sachs
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_book_library.setLayoutManager(gridLayoutManager);
        recyclerview_book_library.setHasFixedSize(true);

        HashMap<String,String> user = sessionManager.getUserDetail();
        fetchUser(user.get(sessionManager.ID));
        recyclerview_book_library.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerview_book_library, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CTHD cthd = listLibrary.get(position);
                String masach = String.valueOf(cthd.getMasach());
                String tensach = cthd.getTensach();
                Intent intent = new Intent(getContext(), BookDetailLibActivity.class);
                intent.putExtra("masach",masach);
                intent.putExtra("tensach",tensach);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;

    }
    public void fetchUser(String key){
        apiInTerFaceHoadon = ApiClient.getApiClient().create(ApiInTerFaceHoadon.class);
        Call<List<CTHD>> call = apiInTerFaceHoadon.get_library_user(key);

        call.enqueue(new Callback<List<CTHD>>() {
            @Override
            public void onResponse(Call<List<CTHD>> call, Response<List<CTHD>> response) {
                progressBar.setVisibility(View.GONE);
                listLibrary= response.body();
                libraryAdapter = new LibraryAdapter(getContext(), listLibrary);
                recyclerview_book_library.setAdapter(libraryAdapter);
                libraryAdapter.notifyDataSetChanged();

                if (listLibrary.size()==0){
                    txtLib_empty.setVisibility(View.VISIBLE);
                    recyclerview_book_library.setVisibility(View.GONE);
                }else {
                    txtLib_empty.setVisibility(View.GONE);
                    recyclerview_book_library.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<CTHD>> call, Throwable t) {
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

}
