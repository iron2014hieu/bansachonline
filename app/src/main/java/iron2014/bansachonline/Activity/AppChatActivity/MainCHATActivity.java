package iron2014.bansachonline.Activity.AppChatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import iron2014.bansachonline.R;

public class MainCHATActivity extends AppCompatActivity {

Adapterchat adapterchat;
List<Modelchat> modelchatList;
RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        recyclerView = findViewById(R.id.RecyclerView_tenchat);
        modelchatList = new ArrayList<>();
        modelchatList.add(new Modelchat("luân óc chó","Tuấn óc cứt ","hiếu óc cứt","said"));
        adapterchat= new Adapterchat(getApplicationContext(),modelchatList);
        recyclerView.setAdapter(adapterchat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
