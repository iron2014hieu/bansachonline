package iron2014.bansachonline;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import iron2014.bansachonline.Fragment.donhang.ChoLayHangFragment;
import iron2014.bansachonline.Fragment.donhang.ChoXacNhanFragment;
import iron2014.bansachonline.Fragment.donhang.DangGiaoFragment;
import iron2014.bansachonline.Fragment.donhang.DanhGiaFragment;
import iron2014.bansachonline.adapter.ViewPagerFM.TabViewPagerAdapter;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;

public class MuahangActivity extends AppCompatActivity {
    TabViewPagerAdapter tabViewPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabLayout.Tab tab;
    String check;
    int select =2;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_muahang);
        viewPager = findViewById(R.id.fragment_container_muahang);
        tabLayout = findViewById(R.id.tablayout_id);
        Toolbar toolbar = findViewById(R.id.toolbar_muahang);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        check = intent.getStringExtra("check");

        tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        //add fragmenr here
        tabViewPagerAdapter.AddFragment(new ChoXacNhanFragment(), "Chờ xác nhận");
        tabViewPagerAdapter.AddFragment(new ChoLayHangFragment(), "Chờ lấy hàng");
        tabViewPagerAdapter.AddFragment(new DangGiaoFragment(), "Đang giao");
        tabViewPagerAdapter.AddFragment(new DanhGiaFragment(), "Đánh giá");
        viewPager.setAdapter(tabViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if (check==null){
            tab = tabLayout.getTabAt(0);
            tab.select();
        }else {
            tab = tabLayout.getTabAt(Integer.valueOf(check));
            tab.select();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = (new Intent(getApplicationContext(), MainActivity.class));
        intent.putExtra("check", "5");
        startActivity(intent);
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
        return  true;
    }
}
