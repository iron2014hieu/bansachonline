package iron2014.bansachonline.Activity.AppChatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;

public class HienthiTnActivity extends AppCompatActivity {
           RecyclerView recyclerView;
    List<Modeltinnhan> modeltinnhanList;
    Adaptertinnhan adaptertinnhan;
    Button btnsendmess;
    DatabaseReference mData;
    EditText editsendmesss;
    SessionManager sessionManager;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_hienthi_tn);
        recyclerView = findViewById(R.id.recyclerview_tn);
        btnsendmess = findViewById(R.id.btnSendMsg);
        editsendmesss = findViewById(R.id.etMessage);
        if (editsendmesss.getText().toString().isEmpty()){
            btnsendmess.setVisibility(View.GONE);
        }
        editsendmesss.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.equals("")){
                    btnsendmess.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")){
                    btnsendmess.setVisibility(View.VISIBLE);
                }else if (s.equals("")){
                    btnsendmess.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.equals("")){
                    btnsendmess.setVisibility(View.GONE);
                }
            }
        });
        btnsendmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email;
                sessionManager = new SessionManager(HienthiTnActivity.this);
                HashMap<String,String> user = sessionManager.getUserDetail();
                email= user.get(sessionManager.EMAIL);

                mData = FirebaseDatabase.getInstance().getReference();
                String text_send_message = editsendmesss.getText().toString();

                Modeltinnhan modeltinnhan = new Modeltinnhan(email,text_send_message,"trieu","1");
                mData.child("Message").push().setValue(modeltinnhan);
                editsendmesss.setText("");
                btnsendmess.setVisibility(View.GONE);
            }
        });
        modeltinnhanList = new ArrayList<>();
        Query query =FirebaseDatabase.getInstance().getReference("Message").orderByChild("idchattt").equalTo("1");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Modeltinnhan modeltinnhan =  dataSnapshot.getValue(Modeltinnhan.class);
                modeltinnhanList.add(modeltinnhan);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(HienthiTnActivity.this,LinearLayoutManager.VERTICAL,false);
                layoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                adaptertinnhan = new Adaptertinnhan(HienthiTnActivity.this, modeltinnhanList);
                recyclerView.setAdapter(adaptertinnhan);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
}
