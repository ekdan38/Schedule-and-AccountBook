package com.example.scheduleandaccountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Select extends AppCompatActivity {
    private FirebaseAuth auth; //파이어베이스 인증 객체
    private DatabaseReference uDatabaseRef; // 실시간 데이터베이스
    private DatabaseReference peDatabaseRef;
    private DatabaseReference puDatabaseRef;

    private void joinGroup(String inviteCode, String memberUid) {
//        DatabaseReference groupsRef = FirebaseDatabase.getInstance().getReference("groups");
        puDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("Public_User_DB");

        puDatabaseRef.orderByChild("inviteCode").equalTo(inviteCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // A group with the given invite code exists
                    DataSnapshot groupSnapshot = dataSnapshot.getChildren().iterator().next();
                    String groupId = groupSnapshot.getKey();

                    // Add the member to the group
                    DatabaseReference membersRef = puDatabaseRef.child(groupId).child("members");
                    membersRef.child(memberUid).setValue(true);
                } else {
                    // The invite code is invalid
                    Toast.makeText(Select.this,"잘못된 초대 코드입니다.",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
                ///////////////////////////////////////////////
    public Button btn_personal_mode, btn_public_mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        uDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("UserAccount");
        peDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("Personal_User_DB");

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