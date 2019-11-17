package iron2014.bansachonline;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import iron2014.bansachonline.Fragment.FirstStartApp.FirstStart1Fragment;
import iron2014.bansachonline.Fragment.FirstStartApp.FirstStart2Fragment;
import iron2014.bansachonline.Fragment.FirstStartApp.FirstStart3Fragment;
import iron2014.bansachonline.adapter.ViewPagerFM.TabViewPagerAdapter;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;

public class FirstStartActivity extends AppCompatActivity {
    TabViewPagerAdapter tabViewPagerAdapter;
    ViewPager viewPager;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_first_start);
        viewPager = findViewById(R.id.fragment_container_first_appstart);

        tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        //add fragmenr here
        tabViewPagerAdapter.AddFragment(new FirstStart1Fragment(), "");
        tabViewPagerAdapter.AddFragment(new FirstStart2Fragment(), "");
        tabViewPagerAdapter.AddFragment(new FirstStart3Fragment(), "");
        viewPager.setAdapter(tabViewPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);

    }
    //settheme
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
}
