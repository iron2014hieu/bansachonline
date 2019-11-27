package iron2014.bansachonline;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import iron2014.bansachonline.Activity.SearchBooksActivity;
import iron2014.bansachonline.fragmentVanChuyen.Activity.ShipperActivity;
import iron2014.bansachonline.LoginRegister.LoginActivity;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.URL.UrlSql;
import iron2014.bansachonline.adapter.ViewPagerFM.FragmentAdapter;
import iron2014.bansachonline.fragmentMain.CanhanFragment;
import iron2014.bansachonline.fragmentMain.HomeFragment;
import iron2014.bansachonline.fragmentMain.LibraryFragment;
import iron2014.bansachonline.fragmentMain.NotificationFragment;
import iron2014.bansachonline.fragmentMain.TheloaiFragment;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPref sharedPref;
    SessionManager sessionManager;
    BottomNavigationView navigation;
    ViewPager viewPager;
//    private TextView textNotify;
//    private ImageButton cartButtonIV;
    private Button btnSearchView;
    private UrlSql urlSql;
    LinearLayout linearLayoutMain;
    public  static SharedPreferences prefs;
    public static boolean firstStart;
    String id,name;
    TabLayout.Tab tab;
    public  static ClipboardManager clipboardManager;
    public static ClipData clipData;
    boolean doubleBackToExitPressedOnce = false;
    String quyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        theme();
        setAppLocale(sharedPref.loadLanguage());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        textNotify=findViewById(R.id.textNotify);
//        cartButtonIV= findViewById(R.id.cartButtonIV);
        linearLayoutMain= findViewById(R.id.linearLayoutM);
        navigation = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewpager); //Idnit Viewpager
//        btnSearchView = findViewById(R.id.btnSearch);
        clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);


        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart){
            ShowFirstAppStart();
        }
//        Toobar đã như ActionBar
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();



        sessionManager = new SessionManager(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        viewPager.setPageTransformer(true, new CubeTransformer());

        setupFm(getSupportFragmentManager(), viewPager); //Setup Fragment
        viewPager.setCurrentItem(0); //Set Currrent Item When Activity Start
        viewPager.setOnPageChangeListener(new PageChange()); //Listeners For Viewpager When Page Changed
        Intent intent = getIntent();
        String check = intent.getStringExtra("check");
        if (check != null) {
            if (check.equals("4")) {
                viewPager.setCurrentItem(4);
                navigation.setSelectedItemId(R.id.nav_notif);
            }else if (check.equals("5")){
                viewPager.setCurrentItem(5);
                navigation.setSelectedItemId(R.id.nav_profile);
            }else if (check.equals("2")){
                viewPager.setCurrentItem(2);
                navigation.setSelectedItemId(R.id.nav_search);
            }else if (check.equals("3")){
                viewPager.setCurrentItem(3);
                navigation.setSelectedItemId(R.id.nav_notif);
            }else if (check.equals("1")){
                viewPager.setCurrentItem(1);
                navigation.setSelectedItemId(R.id.nav_library);
            }
        }

            HashMap<String,String> user = sessionManager.getUserDetail();
            name = user.get(sessionManager.NAME);
            id = user.get(sessionManager.ID);
            quyen = user.get(sessionManager.QUYEN);
            if (!InternetConnection.checkConnection(getApplicationContext())) {
                Snackbar.make(linearLayoutMain,
                        R.string.string_internet_connection_not_available,
                        Snackbar.LENGTH_LONG).show();
            }
            if (quyen!=null&&quyen.equals("shipper")){
                startActivity(new Intent(getBaseContext(), ShipperActivity.class));
                finish();
            }
            if (quyen!=null&&quyen.equals("shipper")){
                startActivity(new Intent(getBaseContext(), ShipperActivity.class));
            }

        //Setup seerch view
//        btnSearchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), SearchBooksActivity.class));
//            }
//        });

    }
    // thay đổi ngôn ngữ
    private void setAppLocale(String localeCode){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf =    res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(localeCode.toLowerCase()));
        }else {
            conf.locale = new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf, dm);
    }
    public static void setupFm(FragmentManager fragmentManager, ViewPager viewPager){
        FragmentAdapter Adapter = new FragmentAdapter(fragmentManager);
        //Add All Fragment To List
        Adapter.add(new HomeFragment(), "Trang chủ");
        Adapter.add(new TheloaiFragment(), "The loại");
        Adapter.add(new LibraryFragment(), "Tìm kiếm");
        Adapter.add(new NotificationFragment(), "Thông báo");
        Adapter.add(new CanhanFragment(), "Cá nhân");
        viewPager.setAdapter(Adapter);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nav_library:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.nav_search:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.nav_notif:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.nav_profile:
                    viewPager.setCurrentItem(4);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.cartButtonIV:
//                startActivity(new Intent(getBaseContext(), Main2Activity.class));
//                break;
        }
    }

    public class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.nav_home);
                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.nav_library);
                    break;
                case 2:
                    navigation.setSelectedItemId(R.id.nav_search);
                    break;
                case 3:
                    navigation.setSelectedItemId(R.id.nav_notif);
                    break;
                case 4:
                    navigation.setSelectedItemId(R.id.nav_profile);
                    break;
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }


    //settheme
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        val itemData = menu.findItem(R.id.action_addcart)
//        val actionView = itemData.actionView as CartCounterActionView
//        actionView.setItemData(menu, itemData)
//        actionView.setCount(cartCount)
//        return super.onPrepareOptionsMenu(menu)
        if (name != null) {
            switch (item.getItemId()) {
                case R.id.action_addcart:
                    startActivity(new Intent(MainActivity.this, Main2Activity.class));
                    break;
            }
        } else {
            switch (item.getItemId()) {
                case R.id.action_addcart:
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void GetDataCouterCart(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        item_count = response.length();
//                        textNotify.setText(String.valueOf(item_count));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn thêm lần nữa để thoát!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
//        textNotify.setText(String.valueOf(item_count));
        super.onResume();
    }
    private void ShowFirstAppStart(){
        startActivity(new Intent(getBaseContext(), FirstStartActivity.class));
        //set boolearn check first start app to false
//        SharedPreferences.Editor editor =prefs.edit();
//        editor.putBoolean("firstStart", false);
//        editor.apply();
    }
}
