package iron2014.bansachonline.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import iron2014.bansachonline.CartDetailActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.fragmentMain.TheloaiFragment;

public class ChonMaKhuyenmaiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_ma_khuyenmai);

        // Create new fragment and transaction
        Fragment newFragment = new TheloaiFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.container_chonmakm, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), CartDetailActivity.class));
    }
}
