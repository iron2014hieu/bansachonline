package iron2014.bansachonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    Button btnThuvien, btnHome;
    Toolbar toolbar_reload;
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

        btnHome = findViewById(R.id.btnHome);
        btnThuvien = findViewById(R.id.btnThuvien);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MuahangActivity.this, MainActivity.class));
            }
        });
        btnThuvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MuahangActivity.this, MainActivity.class);
                i.putExtra("check", "2");
                startActivity(i);
            }
        });

        toolbar_reload = findViewById(R.id.toolbar_reload);
        toolbar_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MuahangActivity.this, MuahangActivity.class));
            }
        });


        tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        //add fragmenr here
        tabViewPagerAdapter.AddFragment(new ChoXacNhanFragment(), getString(R.string.choxacnhan));
        tabViewPagerAdapter.AddFragment(new ChoLayHangFragment(), getString(R.string.cholayhang));
        tabViewPagerAdapter.AddFragment(new DangGiaoFragment(), getString(R.string.danggiao));
        tabViewPagerAdapter.AddFragment(new DanhGiaFragment(), getString(R.string.danhgia));
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
        finish();
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
