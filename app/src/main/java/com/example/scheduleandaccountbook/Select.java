package com.example.scheduleandaccountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Select extends AppCompatActivity {
    private FirebaseAuth auth; //파이어베이스 인증 객체
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private DatabaseReference pDatabaseRef;

    public Button btn_personal_mode, btn_public_mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SAA").child("UserAccount");
        pDatabaseRef = FirebaseDatabase.getInstance().getReference("SAA").child("Personal_User_DB");

//        String userIdToken = FirebaseAuth.getInstance().getCurrentUser().getIdToken(0);


        btn_personal_mode = findViewById(R.id.btn_personal_mode);//개인 사용 모드
        btn_personal_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Personal_mode_menu.class);//일단은 메뉴로 넘어가게
                startActivity(intent);
            }
        });

        btn_public_mode = findViewById(R.id.btn_public_mode);//모임 방 사용 모드
        btn_public_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Public_mode_menu.class);//일단은 메뉴로 넘어가게
                startActivity(intent);
            }
        });
    }
}