package iron2014.bansachonline.LoginRegister;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.HashMap;
import java.util.Locale;

import iron2014.bansachonline.MainActivity;
import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;

public class SettingsActivity extends AppCompatActivity {
    private Switch mySwitch;
    SharedPref sharedPref;
    TextView txtThayLang;

    SessionManager sessionManager;
    Toolbar toolbar_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
        setAppLocale(sharedPref.loadLanguage());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        txtThayLang = findViewById(R.id.txtChangeLang);
        mySwitch = (Switch)findViewById(R.id.mySwitch);
        toolbar_setting=(Toolbar)findViewById(R.id.toolbar_setting);
        toolbar_setting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        if (sharedPref.loadNightModeState()==true){
            mySwitch.setChecked(true);
        }
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sharedPref.setNightModelState(true);
                    restartApp();
                }else {
                    sharedPref.setNightModelState(false);
                    restartApp();
                }
            }
        });
        if (sharedPref.loadLanguage().equals("vi")){
            txtThayLang.setText(getString(R.string.tiengviet));
        }else if (sharedPref.loadLanguage().equals("en")){
            txtThayLang.setText("English");
        }else if (sharedPref.loadLanguage().equals("ar")){
            txtThayLang.setText("Arab ");
        }
        //lấy thể loại
        sessionManager = new SessionManager(this);
        HashMap<String,String> theloai = sessionManager.getMAtheloai();
        String matheloai = theloai.get(sessionManager.MATHELOAI);
    }

    public void  restartApp(){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent = (new Intent(getApplicationContext(), MainActivity.class));
        intent.putExtra("check", "5");
        startActivity(intent);
    }
    public void showChangeLanguageDialog(View view) {
        final String[] lisItems={"Vietnamese","English"};
        AlertDialog.Builder mbuilder=new AlertDialog.Builder(SettingsActivity.this);
        mbuilder.setTitle(R.string.app_name);
        mbuilder.setSingleChoiceItems(lisItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0){
                    //Tiếng việt
                    setAppLocale("vi");
                    sharedPref.setLanguage("vi");
                    recreate();
                }else if (i==1){
                    //Tiếng anh
                    setAppLocale("en");
                    sharedPref.setLanguage("en");
                    recreate();
                }
                //off in
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog=mbuilder.create();
        //Show dialog
        mDialog.show();
    }
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
}
