package iron2014.bansachonline.Fragment.FirstStartApp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstStart3Fragment extends Fragment {

    Button btnStartApp;
    public FirstStart3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_start3, container, false);
        btnStartApp= view.findViewById(R.id.btnStartApp);

        btnStartApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set boolearn check first start app to false
                SharedPreferences.Editor editor = MainActivity.prefs.edit();
                editor.putBoolean("firstStart", false);
                editor.apply();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
        return view;
    }
}
