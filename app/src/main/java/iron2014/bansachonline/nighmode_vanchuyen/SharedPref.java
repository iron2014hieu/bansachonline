package iron2014.bansachonline.nighmode_vanchuyen;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences myPreferences;
    public SharedPref(Context context){
        myPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
        //this mode save true/false
    }
    public void setNightModelState(Boolean state){
        SharedPreferences.Editor editor= myPreferences.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }
    //this mode will load The noght mode
    public Boolean loadNightModeState(){
        Boolean state = myPreferences.getBoolean("NightMode", false);
        return state;
    }

    public void setLanguage(String lang){
        SharedPreferences.Editor editor= myPreferences.edit();
        editor.putString("language", lang);
        editor.commit();
    }
    //this mode will load The noght mode
    public String loadLanguage(){
        String lang = myPreferences.getString("language", "vi");
        return lang;
    }
}
