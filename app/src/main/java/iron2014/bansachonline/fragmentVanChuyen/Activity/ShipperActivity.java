package iron2014.bansachonline.fragmentVanChuyen.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import iron2014.bansachonline.Fragment.donhang.ChoXacNhanFragment;
import iron2014.bansachonline.LoginRegister.ProfileActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.adapter.ViewPagerFM.TabViewPagerAdapter;
import iron2014.bansachonline.fragmentVanChuyen.GiaoHangFragment;
import iron2014.bansachonline.fragmentVanChuyen.NhanHangFragment;

public class ShipperActivity extends AppCompatActivity {
    TabViewPagerAdapter tabViewPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabLayout.Tab tab;
    ImageView btnLogoutvc;
    String check;
    int select =2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper);
        viewPager = findViewById(R.id.fragment_container_vanchuyen);
        tabLayout = findViewById(R.id.tablayout_id);
        Toolbar toolbar = findViewById(R.id.toolbar_vanchuyen);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        check = intent.getStringExtra("check");
        btnLogoutvc = findViewById(R.id.btnLogoutvc);
        btnLogoutvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
        tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        //add fragmenr here
        tabViewPagerAdapter.AddFragment(new ChoXacNhanFragment(), "Chờ xác nhận");
        tabViewPagerAdapter.AddFragment(new GiaoHangFragment(), "Giao hàng");
        tabViewPagerAdapter.AddFragment(new NhanHangFragment(), "Nhận giao");
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
       //startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }
}
